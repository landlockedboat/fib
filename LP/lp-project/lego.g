
#header
<<
#include <string>
#include <iostream>
#include <map>
using namespace std;

// struct to store information about tokens
typedef struct {
  string kind;
  string text;
} Attrib;

// function to fill token information (predeclaration)
void zzcr_attr(Attrib *attr, int type, char *text);

// fields for AST nodes
#define AST_FIELDS string kind; string text;
#include "ast.h"

// macro to create a new AST node (and function predeclaration)
#define zzcr_ast(as,attr,ttype,textt) as=createASTnode(attr,ttype,textt)
AST* createASTnode(Attrib* attr,int ttype, char *textt);
>>

<<
#include <cstdlib>
#include <cmath>
//global structures
AST *root;


// function to fill token information
void zzcr_attr(Attrib *attr, int type, char *text) {
  if (type == ID) {
    attr->kind = "id";
    attr->text = text;
  }
  else if (type == NUM) {
    attr->kind = "intconst";
    attr->text = text;
  }
  else if (type == NORTH || type == SOUTH || type == EAST || type == WEST) {
    attr->kind = "direction";
    attr->text = text;
  }
  else if (type == POP || type == PUSH) {
    attr->kind = "stacker";
    attr->text = text;
  }
  else {
    attr->kind = text;
    attr->text = "";
  }
}

// function to create a new AST node
AST* createASTnode(Attrib* attr, int type, char* text) {
  AST* as = new AST;
  as->kind = attr->kind;
  as->text = attr->text;
  as->right = NULL;
  as->down = NULL;
  return as;
}


/// create a new "list" AST node with one element
AST* createASTlist(AST *child) {
 AST *as=new AST;
 as->kind="list";
 as->right=NULL;
 as->down=child;
 return as;
}

/// get nth child of a tree. Count starts at 0.
/// if no such child, returns NULL
AST* child(AST *a,int n) {
AST *c=a->down;
for (int i=0; c!=NULL && i<n; i++) c=c->right;
return c;
}



/// print AST, recursively, with indentation
void ASTPrintIndent(AST *a,string s)
{
  if (a==NULL) return;

  cout<<a->kind;
  if (a->text!="") cout<<"("<<a->text<<")";
  cout<<endl;

  AST *i = a->down;
  while (i!=NULL && i->right!=NULL) {
    cout<<s+"  \\__";
    ASTPrintIndent(i,s+"  |"+string(i->kind.size()+i->text.size(),' '));
    i=i->right;
  }

  if (i!=NULL) {
      cout<<s+"  \\__";
      ASTPrintIndent(i,s+"   "+string(i->kind.size()+i->text.size(),' '));
      i=i->right;
  }
}

/// print AST
void ASTPrint(AST *a)
{
  while (a!=NULL) {
    cout<<" ";
    ASTPrintIndent(a,"");
    a=a->right;
  }
}



//  ###### #    #   ##   #      #    #   ##   ##### ######
//  #      #    #  #  #  #      #    #  #  #    #   #
//  #####  #    # #    # #      #    # #    #   #   #####
//  #      #    # ###### #      #    # ######   #   #
//  #       #  #  #    # #      #    # #    #   #   #
//  ######   ##   #    # ######  ####  #    #   #   ######

//Let us understand what all of this means
//node -> right   : you are accessing to your right brother
//node -> down    : you are accessing yo your first child
//node -> kind    : the content the node has on the tree
//child(node, n)  : returs the nth child of node
//and that's all. You cannot do the following:
//node -> left
//node -> up

#include <vector>
#include <string>
#include <iomanip>
#include <iterator>
#include <list>
#include <sstream> //to_string

using namespace std;

typedef struct {
  int x, y;
} dcoord;

typedef struct {
  string id;
  int x, y;   // top-left corner of the block
  int w, h;    // block dimensions
  //I'd like to initilise those, but compiler complains.
  //As an agreement, if a is "-" means that no block is above it
  string a;
} dblock;

typedef list <string> btower;

typedef struct {
  int n, m;
  //matrix where we store the total height of every block part at a given point
  vector< vector<int> > height;
  //matrix where we store the positions of every block part with its associated id
  vector< vector<string> > ids;
  //data structure that keeps track of all blocks in our Grid
  map<string, dblock>   blocks;
} Grid;

Grid g;
//Data structure to store function definitions
map <string, AST*> functions;
//This is all for printing stuff with pretty colors
std::ostream& bold_on   (std::ostream& os){ return os << "\e[1m"; }
std::ostream& bold_off  (std::ostream& os){ return os << "\e[0m"; }
std::ostream& green     (std::ostream& os){ return os << "\e[32m";}
std::ostream& red       (std::ostream& os){ return os << "\e[31m";}
std::ostream& blue      (std::ostream& os){ return os << "\e[34m";}
std::ostream& white     (std::ostream& os){ return os << "\e[37m";}
std::ostream& bg_red    (std::ostream& os){ return os << "\e[41m";}
std::ostream& bg_blue   (std::ostream& os){ return os << "\e[44m";}
std::ostream& bg_green  (std::ostream& os){ return os << "\e[42m";}
std::ostream& bg_yellow (std::ostream& os){ return os << "\e[43m";}
std::ostream& bg_black  (std::ostream& os){ return os << "\e[40m";}

std::ostream& normal    (std::ostream& os){ return os << "\e[0;37;39m";}
std::ostream& tab       (std::ostream& os){return os << "   ";}

void printKindError(string functionName, string gotKind, string expectedKind){
  cout << red << "ERROR" << normal <<": Unexpected kind at function " << blue <<
  functionName << normal << " got " << red << gotKind << normal <<
  " expected " << green << expectedKind << normal << endl;
}

void printBlock(dblock db){
  cout << green << bold_on << db.id << normal  << " {"<< endl;
  cout << bold_on << setw(6) << "a" << normal << ": " << db.a << endl;
  cout << red << bold_on << tab << "top-left corner" << normal << ": "<< endl;
  cout << bold_on << setw(6) << "x" << normal << ": " << db.x + 1 << endl;
  cout << bold_on << setw(6) << "y" << normal << ": " << db.y + 1 << endl;
  cout << red << bold_on << tab << "width and height" << normal << ": " << endl;
  cout << bold_on << setw(6) << "w" << normal << ": " << db.w << endl;
  cout << bold_on << setw(6) << "h" << normal << ": " << db.h << endl;
  cout << "}" << endl;
}


void printHeightMap(){
  vector < vector<int> > heightMap = g.height;
  cout << green << bold_on;
  cout << setw(3) << " ";
  for (int i = 0; i < heightMap.size(); i++){
    cout << setw(3) << i+1;
  }

  cout << normal << endl;

  for (int i = 0; i < heightMap[0].size(); i++){
    cout << bold_on << green << setw(3) << i+1 << normal;
    for (int j = 0; j < heightMap.size(); j++) {
      if(heightMap[j][i] > 2)
        cout << bold_on << bg_red;
      else if(heightMap[j][i] > 1)
        cout << bold_on << bg_yellow;
      else if(heightMap[j][i] > 0)
        cout << bold_on << bg_green;
      cout << setw(3) << heightMap[j][i] << normal;
    }
    cout << endl;
  }
}

void printIdMap(){
  vector< vector<string> > idMap = g.ids;
  cout << green << bold_on;
  cout << setw(3) << " ";
  for (int i = 0; i < idMap.size(); i++){
    cout << setw(3) << i+1;
  }

  int maxColorindex = 5;

  cout << normal << endl;
  for (int i = 0; i < idMap[0].size(); i++){
    cout << bold_on << green << setw(3) << i+1 << normal;
    for (int j = 0; j < idMap.size(); j++) {
      if(idMap[j][i] != "  "){
        int colorHash = idMap[j][i].c_str()[idMap[j][i].length() - 1] +
                        idMap[j][i].c_str()[0];
        colorHash %= maxColorindex;
        switch (colorHash) {
          case 0:
          cout << bg_red;
          break;
          case 1:
          cout << bg_green;
          break;
          case 2:
          cout << bg_blue;
          break;
          case 3:
          cout << bg_yellow;
          break;
          default:
          cout << bg_red;
          break;
        }
        cout << bold_on;
      }
      cout << setw(3) << idMap[j][i] << normal;
    }
    cout << endl;
  }
}

void eraseFromIdMap(dblock db){
  int x = db.x;
  int y = db.y;
  int w = db.w;
  int h = db.h;
  for (int i = x; i < w + x; i++) {
    for (int j = y; j < h + y; j++) {
      if(g.ids[i][j] == db.id)
        g.ids[i][j] = "  ";
    }
  }
}

void addToIdMap(dblock db){
  int x = db.x;
  int y = db.y;
  int w = db.w;
  int h = db.h;
  for (int i = x; i < w + x; i++) {
    for (int j = y; j < h + y; j++) {
      if(g.ids[i][j] != "  ")
        g.ids[i][j] = "!!";
      else
        g.ids[i][j] = db.id;
    }
  }
}

void moveBlock(string direction, int value, dblock& db){
  if(direction  == "NORTH"){
    db.y-=value;
  }
  else if(direction == "SOUTH"){
    db.y+=value;
  }
  else if(direction == "EAST"){
    db.x+=value;
  }
  else if(direction == "WEST"){
    db.x-=value;
  }
}


string toString(dcoord dc){
  stringstream coord;
  coord << dc.x << ',' << dc.y;
  return coord.str();
}

dcoord toCoord(string coord){
  dcoord dc;
  int cindx = coord.find(",");
  int clen =  coord.length();
  dc.x = atoi(coord.substr(0, cindx).c_str());
  dc.y = atoi(coord.substr(cindx + 1, clen - (cindx + 1)).c_str());
  return dc;
}

inline bool isDigit(char c)
{
   return '0' <= c && c <= '9';
}

inline bool isLetter(char c){
  return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
}

void eraseFromHeightMap(dblock db){
  int x = db.x;
  int y = db.y;
  int w = db.w;
  int h = db.h;
  for (int i = x; i < w + x; i++)
    for (int j = y; j < h + y; j++) {
      g.height[i][j] = 0;
    }
}

void addToHeightMap(dblock db){
  int x = db.x;
  int y = db.y;
  int w = db.w;
  int h = db.h;
  for (int i = x; i < w + x; i++)
    for (int j = y; j < h + y; j++) {
      ++g.height[i][j];
    }
  if(db.a != "-")
    addToHeightMap(g.blocks[db.a]);
}



dblock coordBlock(string id){
    dblock ret;
    ret.id = id;
    dcoord dimensions = toCoord(id);
    ret.w = dimensions.x;
    ret.h = dimensions.y;
    ret.a = "-";
    ret.x = 0;
    ret.y = 0;
    return ret;
}

inline bool blockExists(string id){
  return g.blocks.find(id) != g.blocks.end();
}

void setBlock(string id, dblock value){
  if(blockExists(id))
      eraseFromHeightMap(g.blocks[id]);
  g.blocks[id] = value;
  addToHeightMap(g.blocks[id]);
}

dblock getBlock(string id){
  if(!blockExists(id)){
    //We return a coord block in case we were asking
    //for a pseudo id of coordinates
    return coordBlock(id);
  }
  return g.blocks[id];
}

dblock generateTowerBlock(string id){
  //If it doesnt exist we assume its a coord block
  if(!blockExists(id)){
    dblock ret;
    ret = coordBlock(id);
    stringstream blsize;
    blsize << g.blocks.size();
    ret.id = id +":"+ blsize.str();
    setBlock(ret.id, ret);
    return ret;
  }
  else
    return getBlock(id);
}

void initGrid(int width, int height){
  g.n = width;
  g.m = height;
  g.height  = vector < vector <int> > (g.n,  vector <int> (g.m));
  g.ids     = vector < vector <string> > (g.n,  vector <string> (g.m, "  "));
}

int evaluateInt(AST *a){
  if(a->kind == "intconst"){
    return atoi(a->text.c_str());
  }
  else if(a->kind == "HEIGHT"){
    int x = getBlock(child(a,0)->text).x;
    int y = getBlock(child(a,0)->text).y;
    return g.height[x][y];
  }
  printKindError("evaluateInt", a->kind, "intconst");
  return -1;
}

dcoord evaluateCoord(AST *a){
  dcoord dc;
  if(a->kind == "list"){
    dc.x = evaluateInt(child(a,0));
    dc.y = evaluateInt(child(a,1));
    return dc;
  }
  //else
  printKindError("evaluateCoord", a->kind, "list");
  return dc;
}

bool isSpaceEmpty(dblock db){
  int x = db.x;
  int y = db.y;
  int w = db.w;
  int h = db.h;
  for (int i = x; i < w + x; i++) {
    for (int j = y; j < h + y; j++) {
      if(g.ids[i][j] != "  "){
          if(g.ids[i][j] != db.id)
            return false;
        }
      }
    }
  return true;
}

void debugBlock(dblock block){
  cout << "db: " << block.id << " w: " << block.w << " h: " << block.h << endl;
}

void deleteBlock(dblock db){
  if(db.a != "-"){
    deleteBlock(getBlock(db.a));
  }
  g.blocks.erase(db.id);
}

//This function is called when we have asserted that the tower made by a stacker
//expression is correct. This function updates data in our application.
dblock commitTower(btower btow){
  dblock bottomBlock = generateTowerBlock(btow.front());
  btower::iterator tit = btow.end();
  advance(tit, -1);
  dblock aboveBlock = generateTowerBlock(*tit);

  if(aboveBlock.a != "-"){
    deleteBlock(getBlock(aboveBlock.a));
    aboveBlock.a = "-";
    setBlock(aboveBlock.id, aboveBlock);
  }

  while(tit != btow.begin()) {
    eraseFromIdMap(aboveBlock);
    aboveBlock.x = bottomBlock.x;
    aboveBlock.y = bottomBlock.y;

    setBlock(aboveBlock.id, aboveBlock);
    //We go to the block below it
    advance(tit, -1);
    dblock belowBlock;
    if(tit == btow.begin())
      belowBlock = bottomBlock;
    else
      belowBlock = generateTowerBlock(*tit);
    if(belowBlock.a != "-" && belowBlock.a != aboveBlock.id){
      deleteBlock(getBlock(belowBlock.a));
      belowBlock.a = "-";
      setBlock(belowBlock.id, belowBlock);
    }
    belowBlock.a = aboveBlock.id;
    setBlock(belowBlock.id, belowBlock);
    aboveBlock = belowBlock;
  }
  //aboveBlock == bottomBlock
  return aboveBlock;
}

//This function generates a tower by looking at db's "a" fields recursively
//and adding them to a btower
btower buildTower(dblock db){
  btower ret;
  ret.push_back(db.id);
  while(db.a != "-"){
    db = getBlock(db.a);
    ret.push_back(db.id);
  }
  return ret;
}

bool towerIdEquals(string tid, string above){
  if(isDigit(above.at(0))){
    if(isDigit(tid.at(0))){
      string trueID = tid.substr(0, tid.find(":"));
      return trueID == above;
    }
    else
      return false;
  }
  else
    return tid == above;
}

//This function deletes all the blocks above the "above" block
bool removeFromAbove(string above, btower &btow){
  btower::iterator tit = btow.end();
  while (tit != btow.begin()) {
    advance(tit, -1);
    if(towerIdEquals(*tit, above)){
      btow.erase(tit, btow.end());
      return true;
    }
  }
  return false;
}

bool fitsOnTop (dblock above, dblock below){
  bool ret = above.w <= below.w && above.h <= below.h;
  return ret;
}

bool addToTop(string above, btower &btow){
  dblock aboveB = getBlock(above);
  dblock belowB = getBlock(btow.back());
  if(fitsOnTop(aboveB, belowB)){
    btow.push_back(above);
    return true;
  }
  return false;
}

bool evaluateStacker(AST* a, btower &btow){
  string aboveBlock = child(a->right, 0)->text;
  if(aboveBlock == ""){
    aboveBlock = toString(evaluateCoord(child(a->right, 0)));
  }
  string directive = a->right->text;
  AST* nextExpression = child(a->right, 1);
  //Are we in a stacker chain?
  if(nextExpression->kind == "stacker"){
    //We need to execute this function in case we are in a stacker
    //chain to keep our btow updated to the maximum depth
    //of the stacker chain expression
    if(!evaluateStacker(child(a->right, 0), btow)){
      //If the following iteration of executeStacker fails,
      //there is no point in keep going
      return false;
    }
  }
  else{
    //We grab our current brother
    dblock belowBlock = getBlock(nextExpression->text);
    //This function will return a tower built with the blocks above aboveBlock
    //(including itself)
    btow = buildTower(belowBlock);
  }
  if(directive == "POP"){
    //btow is updated in this function
    bool success = removeFromAbove(aboveBlock, btow);
    return success;
  }
  else if(directive == "PUSH"){
    //btow is updated in this function
    bool success = addToTop(aboveBlock, btow);
    return success;
  }
  return false;
}

void trySetBlock(dblock db){
  if(isSpaceEmpty(db)){
    if(blockExists(db.id))
      eraseFromIdMap(getBlock(db.id));
    setBlock(db.id, db);
    addToIdMap(db);
  }
}

dblock evaluateBlock(AST* a, bool& success){
  dblock db;
  string directive = a->right->kind;
  string blockId = a->text;
  if(directive == "PLACE"){
    db.id = blockId;
    dcoord dimensions = evaluateCoord(child(a->right,0));
    db.w = dimensions.x;
    db.h = dimensions.y;
    dcoord tlcorner = evaluateCoord(child(a->right,1));
    //I have to do this because the origin is at 1,1 not 0,0
    db.x = tlcorner.x - 1;
    db.y = tlcorner.y - 1;
    //when we create an empty block, no other blocks are above it
    db.a = "-";
    return db;
  }
  //This means we're on a MOVE instruction
  else if(directive == "direction"){
    string direction = a->right->text;
    int value = atoi(a->right->right->text.c_str());
    db = getBlock(blockId);
    moveBlock(direction, value, db);
    return db;
  }
  //Here we'll kind of "cheat" and change some actual blocks in memory
  //as opposed as only returning a freshly created block as the previous
  //evaluations did
  else if(directive == "stacker"){
    //Btower is used as a temporal way of storing a new hieracy of blocks
    //once we can assure they are correctly placed.
    btower btow;
    if(evaluateStacker(a, btow)){
      success = true;
      //This updates all the blocks inside the tower directly to g.blocks
      return commitTower(btow);
    }
    else{
      success = false;
      dblock trash;
      return trash;
    }


  }
  //else
  printKindError("evaluateBlock", a->kind, "PLACE or direction");
  return db;
}

bool evaluateFits(AST *a){
  dblock below = getBlock(child(a, 0)->text);
  int height = atoi(child(a, 2)->text.c_str());

  if(g.height[below.x][below.y] >= height)
    return false;
  else
  {
    string coord = toString(evaluateCoord(child(a, 1)));
    dblock above = coordBlock(coord);
    return fitsOnTop(above, below);
  }
}

bool evaluateBool(AST *a){
  if(a->kind == "FITS"){
    return evaluateFits(a);
  }
  else if(a->kind == "AND"){
    return evaluateBool(child(a,0)) && evaluateBool(child(a,1));
  }
  else if(a->kind == ">"){
    return evaluateInt(child(a,0)) > evaluateInt(child(a,1));
  }
  else if(a->kind == "<"){
    return evaluateInt(child(a,0)) < evaluateInt(child(a,1));
  }
  return false;
}

void execute(AST *a){
  //When our brother is null, we stop iterating
  if (a == NULL)
    return;
  else if (a->kind == "=" || a->kind == "MOVE"){
    bool success = true;
    dblock db = evaluateBlock(child(a,0), success);
    if(success){
      trySetBlock(db);
    }
  }
  else if (a->kind == "WHILE"){
    while(evaluateBool(child(a, 0)))
       execute(child(a, 1)->down);
  }
  else if(a->kind == "HEIGHT"){
    cout << evaluateInt(a) << endl;
  }
  else if(a->kind == "id"){
    execute(functions[a->text]);
  }
  execute(a->right);
}

void defineFunctions(AST* a){
  while (a != NULL) {
    functions[child(a, 0)->text] = child(a, 1)->down;
    a = a->right;
  }
}

void executeListInstrucctions(AST* a)
{
  //list
  // \__Grid <- we'll be here

  a = child(a,0);
  //list
  // \__Grid              <- we are here
  // |      \__gridWidth  <- we'll work with this one
  // |      \__gridHeight <- and this one

  int gridWidth     = atoi(child(a,0)->text.c_str());
  int gridHeight    = atoi(child(a,1)->text.c_str());
  initGrid(gridWidth, gridHeight);

  //We go down to the main instruction list

  //list
  //  \__Grid         <- we are here
  //  |      \__ ...
  //  \__list         <- we'll be here

  a = a->right;

  //We'll store all function definitions now
  //so we can execute them during runtime
  defineFunctions(a->right);

  //Start instructions execution

  //list
  //  \__Grid
  //  |      \__ ...
  //  \__list         <- we are here
  //         \__...   <- we'll execute this

  execute(child(a,0));

  cout << endl << "Printing blocks..." << endl << endl;
  int blockIndex = 1;
  for (map<string,dblock>::iterator it=g.blocks.begin(); it!=g.blocks.end(); ++it){
    dblock current_b    = it->second;
    cout << "Printing block " << blockIndex << endl << endl;
    printBlock(current_b);
    ++blockIndex;
  }

  cout << endl << "Printing height map..." << endl << endl;
  printHeightMap();
  cout << endl << "Printing id map..." << endl << endl;
  printIdMap();
}


//  ###### #    #   ##   #      #    #   ##   ##### ######
//  #      #    #  #  #  #      #    #  #  #    #   #
//  #####  #    # #    # #      #    # #    #   #   #####
//  #      #    # ###### #      #    # ######   #   #
//  #       #  #  #    # #      #    # #    #   #   #
//  ######   ##   #    # ######  ####  #    #   #   ######


int main() {
  root = NULL;
  ANTLR(lego(&root), stdin);
  ASTPrint(root);
  executeListInstrucctions(root);
}
>>

#lexclass START
#token NUM "[0-9]+"

#token LPAR "\("
#token RPAR "\)"
#token COMMA "\,"

#token LTHAN "\<"
#token BTHAN "\>"
#token AND "AND"

#token GRID "Grid"

#token ASIG "\="

#token WHILE "WHILE"
#token LBRA "\["
#token RBRA "\]"

#token DEF "DEF"
#token ENDEF "ENDEF"

#token PLACE "PLACE"
#token AT "AT"
#token POP "POP"
#token PUSH "PUSH"
#token HEIGHT "HEIGHT"
#token FITS "FITS"

#token MOVE "MOVE"
#token NORTH "NORTH"
#token SOUTH "SOUTH"
#token EAST "EAST"
#token WEST "WEST"

#token ID "[A-Z]+[0-9]*"
#token SPACE "[\ \n]" << zzskip();>>

//Initialisation
lego: grid instructions (definition)* <<#0=createASTlist(_sibling);>>;
//Initilaise the grid
grid: GRID^ NUM NUM;
//Initialise instructions
instructions: (instruction)* <<#0=createASTlist(_sibling);>>;

instruction:  function | id_expr | move | whileloop;

move: MOVE^ ID card number;

id_expr: ID ASIG^ (place | push_pop_expr);

//Place instruction
place: PLACE^ coord AT! coord;

//The push - pop expression defines itself as a right-associative recurrent
//expression.
push_pop_expr:  block (PUSH^|POP^) push_pop_expr_2;
//This is done in order to eliminate ambiguity. Intuitively, this is what
//it would've looked like:
//push_pop_expr_2: (PUSH^|POP^) push_pop_expr_2 | block
push_pop_expr_2: block (((PUSH^|POP^) push_pop_expr_2) | );

//While loop definition
whileloop: WHILE^ LPAR! lexpression RPAR! LBRA! instructions RBRA!;

lexpression: lvalue ((AND^ lvalue) | );

lvalue : fits_func | number (LTHAN^|BTHAN^) number;

//Function syntax
function: fits_func | height_func;
//Again, we do this to avoid ambiguity
parameters: parameter ((COMMA! parameters) | );
parameter: block;

//Fits is a special function that stores its parameters in a very special way
fits_func: FITS^ LPAR! block COMMA! pure_coord COMMA! number RPAR!;

height_func: HEIGHT^ LPAR! ID RPAR!;

//Definition of a block
block: coord | ID;

//Cardinal points
card: NORTH | SOUTH | EAST | WEST;
//Coordinates description
coord: LPAR! pure_coord RPAR!;
//The coordinates without parenthesising
pure_coord: number COMMA! number <<#0=createASTlist(_sibling);>>;
//height returns a number
number: height_func | NUM;
definition: DEF^ ID instructions ENDEF!;
