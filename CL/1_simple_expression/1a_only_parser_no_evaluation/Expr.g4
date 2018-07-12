grammar Expr;

s : e ;
e : DIFF e
  | LPAR e RPAR (OPERATOR e)*
  | e ( MUL | DIV ) e
  | e ( SUM | DIFF ) e
  | INT
  ;

OPERATOR : SUM | DIFF | MUL | DIV | LPAR | RPAR ;

SUM : '+' ;
DIFF : '-' ;
MUL : '*' ;
DIV : '/' ;

LPAR : '(' ;
RPAR : ')' ;

INT : [0-9]+ ;
WS : [ \t\n]+ -> skip ;
