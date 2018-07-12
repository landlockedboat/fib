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
    attr->kind = "ID";
    attr->text = text;
  }
  else if (type == NUM) {
    attr->kind = "NUM";
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

AST* setAsVar(AST *obj) {
 obj->kind="VAR";
 return obj;
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

void ASTParse(AST *a){
  string kind = a->kind;
  string text = a->text;
  cout << "(";
  if(kind == "list")
  {
    cout << "Seq [";
    AST * a2 = a->down;
    while (a2!=NULL) {
      ASTParse(a2);
      a2 = a2->right;
      if(a2!=NULL)
        cout << ", ";
    }
    cout <<"]";
  }
  else if(kind == "ID")
  {
    cout << "\"";
    cout << a->text;
    cout << "\"";
  }
  else if(kind == "VAR")
  {
    cout << "Var \"";
    cout << a->text;
    cout << "\"";
  }
  else if(kind == "NUM")
  {
    cout << "Const ";
    cout << a->text;
  }
  else if(kind == "OR")
  {
    cout << "OR ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);
  }
  else if(kind == "AND")
  {
    cout << "AND ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);
  }
  else if(kind == "NOT")
  {
    cout << "NOT ";
    ASTParse(a->down);
  }
  else if(kind == ":=")
  {
    cout << "Assign ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);
  }
  else if(kind == ">")
  {
    cout << "Gt ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);
  }
  else if(kind == "=")
  {
    cout << "Eq ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);
  }
  else if(kind == "+")
  {
    cout << "Plus ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);
  }
  else if(kind == "-")
  {
    cout << "Minus ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);
  }
  else if(kind == "*")
  {
    cout << "Times ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);
  }
  else if(kind == "INPUT")
  {
    cout << "Input ";
    ASTParse(a->down);
  }
  else if(kind == "PRINT")
  {
    cout << "Print ";
    ASTParse(a->down);
  }
  else if(kind == "EMPTY")
  {
    cout << "Empty ";
    ASTParse(a->down);
  }
  else if(kind == "SIZE")
  {
    cout << "Size ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);
  }
  else if(kind == "PUSH")
  {
    cout << "Push ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);

  }
  else if(kind == "POP")
  {
    cout << "Pop ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);
  }
  else if(kind == "WHILE")
  {
    cout << "Loop ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);
  }
  else if(kind == "IF")
  {
    cout << "Cond ";
    ASTParse(a->down);
    cout << " ";
    ASTParse(a->down->right);
    cout << " ";
    if(a->down->right->right != NULL)
    {
      ASTParse(a->down->right->right);
    }
    else
      cout << "(Seq [])";
  }
  else
    cout << "?" << kind;
  cout << ")";
}


int main() {
  root = NULL;
  ANTLR(input_parser(&root), stdin);
  // ASTPrint(root);
  ASTParse(root);
  cout << endl;
}
>>

#lexclass START

#token INPUT        "INPUT"
#token PRINT        "PRINT"

#token ASSIGN        ":="

#token EMPTY        "EMPTY"
#token SIZE         "SIZE"

#token PUSH         "PUSH"
#token POP          "POP"

#token OR           "OR"
#token AND          "AND"
#token NOT          "NOT"

#token GT        "\>"
#token EQ        "\="
#token PLUS         "\+"
#token MINUS        "\-"
#token TIMES        "\*"

#token IF           "IF"
#token THEN         "THEN"
#token ELSE         "ELSE"
#token END          "END"

#token WHILE        "WHILE"
#token DO           "DO"

#token ID        "[A-Z]+[0-9]*"
#token NUM          "[0-9]+"

#token SPACE "[\ \n]" << zzskip();>>

input_parser: commands;
commands: (command)* <<#0=createASTlist(_sibling);>>;

command: INPUT^ ID | PRINT^ ID | ID ASSIGN^ nExpr | EMPTY^ ID | SIZE^ ID ID | PUSH^ ID nExpr | POP^ ID ID | IF^ bExpr THEN! commands (ELSE! commands | ) END! | WHILE^ bExpr DO! commands END!;

bExpr: bVal ( (AND^ | OR^)  bVal)*;

bVal: (NOT^ |) bVal2;
bVal2: pExpr (GT^ | EQ^) pExpr;

nExpr: tExpr ((PLUS^ | MINUS^) tExpr)*;
tExpr: pExpr (TIMES^ pExpr)*;
pExpr: NUM | var;
var: ID <<#0=setAsVar(_sibling);>>;
