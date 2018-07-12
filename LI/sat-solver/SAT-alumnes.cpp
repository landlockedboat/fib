#include <iostream>
#include <stdlib.h>
#include <algorithm>
#include <vector>
using namespace std;

#define UNDEF -1
#define TRUE 1
#define FALSE 0

// Positive and negative vector
struct PosNegVec {
  vector<int> pos;
  vector<int> neg;
  PosNegVec(){
    pos = vector<int>();
    neg = vector<int>();
  }
};

uint numVars;
uint numClauses;
// This DS holds all clauses and the value of it literals.
// Note that a negative integer means that the literal is
// negated.
vector<vector<int> > clauses;
vector<PosNegVec> watchlists;
vector<int> model;
vector<int> conflicts;
vector<int> modelStack;
uint indexOfNextLitToPropagate;
uint decisionLevel;

void debug(string message){
  cout << message << endl;
}

vector<int>* getWatchlist(int lit){
  if(lit < 0){
    return &watchlists[-lit].pos;
  }
  else {
    return &watchlists[lit].neg;
  }
}

void readClauses(){
  // Skip comments
  char c = cin.get();
  while (c == 'c') {
    while (c != '\n') c = cin.get();
    c = cin.get();
  }
  // Read "cnf numVars numClauses"
  string aux;
  cin >> aux >> numVars >> numClauses;
  clauses.resize(numClauses);
  watchlists.resize(numVars+1, PosNegVec());
  // Read clauses
  for (uint i = 0; i < numClauses; ++i) {
    int lit;
    while (cin >> lit and lit != 0){
      // The variable is not negated
      if(lit >= 0){
        watchlists[lit].pos.push_back(i);
      }
      // lit < 0, which means the variable is negated
      else {
        watchlists[-lit].neg.push_back(i);
      }
      clauses[i].push_back(lit);
    }
  }
}

int currentValueInModel(int lit){
  if (lit >= 0) return model[lit];
  else {
    if (model[-lit] == UNDEF) return UNDEF;
    else return 1 - model[-lit];
  }
}

void setLiteralToTrue(int lit){
  modelStack.push_back(lit);
  if (lit > 0) model[lit] = TRUE;
  else model[-lit] = FALSE;
}

void increaseConflict(int lit){
  if (lit > 0) ++conflicts[lit];
  else ++conflicts[-lit];
}

int getUndefLitWithMostConflicts(){
  int retLit = 0;
  int maxConf = -1;
  for (uint i = 1; i < conflicts.size(); i++){
    if(model[i] == UNDEF and conflicts[i] > maxConf){
      retLit = i;
      maxConf = conflicts[i];
    }
  }
  return retLit;
}

bool propagateGivesConflict ( ) {
  while ( indexOfNextLitToPropagate < modelStack.size() ) {
    int currentLit = modelStack[indexOfNextLitToPropagate];
    // We get the watchlist of the current literal,
    // and we start iterating through it
    vector<int>* wl = getWatchlist(currentLit);
    ++indexOfNextLitToPropagate;
    for (uint idx = 0; idx < wl->size(); ++idx) {
      // We get the clause inside the watchlist
      int i = (*wl)[idx];
      // We have to check this because when we "delete" a clause
      // we are really assigning to it a value of -1, to keep
      // our loops consistent.
      if(i < 0){
        // Skip current iteration if the clause was deleted
        continue;
      }
      bool someLitTrue = false;
      int numUndefs = 0;
      int lastLitUndef = 0;
      for (uint k = 0; not someLitTrue and k < clauses[i].size(); ++k){
        // If there is a literal evaluating to true because
        // of our model, we can bail out of this clause because
        // it generates no conflict.
        int val = currentValueInModel(clauses[i][k]);
        if (val == TRUE) someLitTrue = true;
        else if (val == UNDEF) {
          ++numUndefs;
          lastLitUndef = clauses[i][k];
        }
      }
      // Conflict! All literals evaluate to false.
      if (not someLitTrue and numUndefs == 0)
        return true;
      // Propagation is being executed here.
      // We only propagate if there is no true literals
      // in this clause and if there only is one undef literal.
      else if (not someLitTrue and numUndefs == 1){
        // We set this literal to true to test if it is possible
        // for it to remain this way, because otherwise we'll
        // have a conflict, because it HAS to be true!
        setLiteralToTrue(lastLitUndef);
        increaseConflict(lastLitUndef);
      }
    }
  }
  // All propagation done, no conflicts found.
  return false;
}


void backtrack(){
  uint i = modelStack.size() -1;
  int lit = 0;
  // We go back untill we find the last decision literal.
  // It is found after a '0'.
  while (modelStack[i] != 0){
    lit = modelStack[i];
    model[abs(lit)] = UNDEF;
    modelStack.pop_back();
    --i;
  }
  // At this point, lit is the last decision.
  // Remove the DL mark.
  modelStack.pop_back();
  --decisionLevel;
  indexOfNextLitToPropagate = modelStack.size();
  // Reverse last decision.
  setLiteralToTrue(-lit);
}

// Heuristic for finding the next decision literal:
int getNextDecisionLiteral(){
  return getUndefLitWithMostConflicts();
}

// Checkmodel is only called before outputting that a given solution
// is SATISFIABLE. It is done to prevent any errors that
// the model could have.
void checkmodel(){
  for (uint i = 0; i < numClauses; ++i){
    bool someTrue = false;
    for (uint j = 0; not someTrue and j < clauses[i].size(); ++j){
      someTrue = (currentValueInModel(clauses[i][j]) == TRUE);
    }
    if (not someTrue) {
      cout << "Error in model, clause is not satisfied:";
      for (uint j = 0; j < clauses[i].size(); ++j){
        cout << clauses[i][j] << " ";
      }
      cout << endl;
      exit(1);
    }
  }
}

int main(){
  // reads numVars, numClauses and clauses
  readClauses();
  model.resize(numVars+1,UNDEF);
  conflicts.resize(numVars+1,0);
  indexOfNextLitToPropagate = 0;
  decisionLevel = 0;

  // Take care of initial unit clauses, if any.
  // If a unit clause is false in our model, we return
  // UNSAT
  for (uint i = 0; i < numClauses; ++i)
  if (clauses[i].size() == 1) {
    int lit = clauses[i][0];
    int val = currentValueInModel(lit);
    // I doubt that this could happen right off the
    // initial state.
    if (val == FALSE) {cout << "UNSATISFIABLE" << endl; return 10;}
    else if (val == UNDEF) setLiteralToTrue(lit);
  }

  // DPLL algorithm
  while (true) {
    while ( propagateGivesConflict() ) {
      if ( decisionLevel == 0) { cout << "UNSATISFIABLE" << endl; return 10; }
      backtrack();
    }
    int decisionLit = getNextDecisionLiteral();
    if (decisionLit == 0) { checkmodel(); cout << "SATISFIABLE" << endl; return 20; }
    // Start new decision level:
    // Push mark indicating new DL.
    modelStack.push_back(0);
    ++indexOfNextLitToPropagate;
    ++decisionLevel;
    // Now push decisionLit on top of the mark.
    setLiteralToTrue(decisionLit);
  }
}
