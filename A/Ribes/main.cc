#include "GeneralFunctions.cc"
#include "ShingleSetMaker.cc"
#include "ShingleBagMaker.cc"
#include <iostream>
#include "Bag.h"
#include "CharacteristicMatrix.h"
#include "LSHmaker.cc"
// #include <functional>

// #include <stdlib.h>
// #include <stdio.h>

using namespace std;

// typedef pair<int,int> SetPair;


void testHash() {
  int MAX_MOD = 20;
  for (int mod = 2; mod <= MAX_MOD; ++mod) {
    for (int a = 1; a < mod; ++a) {
      for (int b = 0; b < mod; ++b) {
        cout << "a: " << a << ", b: " << b << ", mod: " << mod << " ==> " << evaluateHashFunction(a,b,mod) << " colisiones";
        if (evaluateHashFunction(a,b,mod) == 0) cout << " ---------- MOD: " << mod;
        cout << endl;
      }
    }
  }
  cout << "Fin test" << endl;
}

unsigned int stringToHash(const string& s) {
  unsigned int address = 0;
  for (int i = 0; i < s.size(); ++i) {
    address = (unsigned int)s[i] + (address << 6) + (address << 16) - address;  
  }
}

void printaSet(const set<SetPair>& s) {
  cout << "Los sets que tenemos son: " << endl;
  for (set<SetPair>::const_iterator it = s.begin(); it != s.end(); ++it) {
//       cout << "7" << endl;
    cout << it->first << " " << it->second << endl;  
//     cout << "8" << endl;
  }
}

int main() {
    cout << "Probamos el LSH" << endl;
    vector<int> v = vector<int> (23, 5);
//     cout << "1" << endl;
    vector<vector<int> > minHash = vector<vector<int> > (4, v);
    for (int i = 0; i < 23; ++i) {
      minHash[2][i] = 3;  
    }
//     cout << "2" << endl;
    //Tendremos 4 sets, cada uno de ellos con 23 elementos, y todos ellos con un 5
    set<SetPair> miset = LSH(minHash, 0.9);
//     cout << "3" << endl;
    printaSet(miset);
}