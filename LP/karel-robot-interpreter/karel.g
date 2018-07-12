#header
<<
#include <string>
#include <iostream>
#include <map>
using namespace std;

typedef struct {
  string kind;
  string text;
} Attrib;

void zzcr_attr(Attrib *attr, int type, char *text);
#define AST_FIELDS string kind; string text;
#include "ast.h"
#define zzcr_ast(as,attr,ttype,textt) as=createASTnode(attr,ttype,textt)
AST* createASTnode(Attrib* attr,int ttype, char *textt);
>>

<<
#include <cstdlib>
#include <cmath>
AST *root;
void zzcr_attr(Attrib *attr, int type, char *text) {
  if (type == ID) {
    attr->kind = "id";
    attr->text = text;
  }
  else {
    attr->kind = text;
    attr->text = "";
 }
}
AST* createASTnode(Attrib* attr, int type, char* text) {
  AST* as = new AST;
  as->kind = attr->kind;
  as->text = attr->text;
  as->right = NULL;
  as->down = NULL;
  return as;
}
AST* createASTlist(AST *child) {
 AST *as=new AST;
 as->kind="list";
 as->right=NULL;
 as->down=child;
 return as;
}

AST* child(AST *a,int n) {
  AST *c=a->down;
  for (int i=0; c!=NULL && i<n; i++) c=c->right;
  return c;
}

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

void ASTPrint(AST *a)
{
  while (a!=NULL) {
    cout<<" ";
    ASTPrintIndent(a,"");
    a=a->right;
  }
}

#include <vector>
#include <string>
AST* findDefinition(string id);

int robotOr;
int robotX, robotY;
int robotBprs;

vector<vector<int> > beepMap;

bool evaluateCondition(AST *a);
bool dinsDominis (int x, int y);
//TOP 1
//LEFT 2
//DOWN 3
//RIGHT 4

bool isClear (int x, int y, int orient);


void applyDirection(int& x, int& y, int direction){
  switch (direction) {
    case 1:
      --y;
    break;
    case 2:
      --x;
    break;
    case 3:
      ++y;
    break;
    //case 4
    default:
      ++x;
    break;
  }
}

void novaPosicio(AST* a){
  if(a == NULL){
    return;
  }
  else if(a->kind == "move"){
    int x, y;
    applyDirection(x, y, robotOr);
    if(dinsDominis(x, y) && isClear(robotX, robotY, robotOr)){
      robotX = x;
      robotY = y;
    }
  }
  else if(a->kind == "turnleft"){
    robotOr = robotOr + 1 > 4 ? 1 : robotOr + 1;
  }
  else if(a->kind == "pickbeeper"){
    if(beepMap[robotX][robotY] >= 1){
      ++robotBprs;
      --beepMap[robotX][robotY];
    }
  }
  else if(a->kind == "iterate"){
    int length = atoi(a->down->kind.c_str());
    for (int i = 0; i < length; i++) {
      novaPosicio(a->down->right->down);
    }
  }
  else if(a->kind == "if"){
    if(evaluateCondition(a->down)){
      novaPosicio(a->down->right->down);
    }
  }
  else if(a->kind == "id"){
    novaPosicio(findDefinition(a->text)->down->right->down);
  }
  else if(a->kind == "turnoff"){
    return;
  }
  a = a->right;
}

int main() {
  root = NULL;
  ANTLR(karel(&root), stdin);
  ASTPrint(root);
}
>>

#lexclass START

#token WORLD        "world"
#token ROBOT        "robot"
#token WALLS        "walls"
#token BEEPERS      "beepers"



#token ISCLEAR      "isClear"
#token ANYBEEPERS   "anyBeepersInBag"

#token TURNLEFT     "turnleft"
#token MOVE         "move"

#token PUTBEEPER    "putbeeper"
#token PICKBEEPER   "pickbeeper"
#token FOUNDBEEPER  "foundbeeper"

#token ITERATE      "iterate"
#token DEFINE       "define"
#token BEGIN        "begin"
#token END          "end"
#token TURNOFF      "turnoff"

#token IF          "if"
#token AND          "and"
#token OR           "or"
#token NOT          "not"

#token RIGHT        "right"
#token LEFT         "left"
#token UP           "up"
#token DOWN         "down"

#token ID           "[A-Z]+[0-9]*"
#token NUM          "[0-9]+"
#token LCLA         "\["
#token RCLA         "\]"
#token LBRA         "\{"
#token RBRA         "\}"
#token COMMA        "\,"
#token SEMI         "\;"

#token SPACE "[\ \n]" << zzskip();>>

karel: world robot inits BEGIN! instrs END!  <<#0=createASTlist(_sibling);>>;

world: WORLD^ NUM NUM;
robot: ROBOT^ NUM NUM NUM direction;

inits: (walls|beepers)* (def)*    <<#0=createASTlist(_sibling);>>;
walls: WALLS^ LCLA! NUM NUM direction (COMMA! NUM NUM direction)* RCLA!;
beepers: BEEPERS^ NUM NUM NUM;

def: DEFINE^ ID LBRA! instrs RBRA!;

instrs: (instruction|specinstr)* <<#0=createASTlist(_sibling);>>;

instruction: (TURNLEFT | MOVE | PUTBEEPER | TURNOFF | ID | PICKBEEPER) SEMI!;

specinstr: condinst | loop;

condinst: IF^ condition LBRA! instrs RBRA!;
condition: lelement ((AND^ | OR^) lelement)*;
lelement : (NOT| ) (ISCLEAR | ANYBEEPERS | FOUNDBEEPER);
loop: ITERATE^ NUM LBRA! instrs RBRA!;

direction: RIGHT | LEFT | UP | DOWN;
