grammar Calc;

prog:   stat+ EOF ;

stat:   expr NEWLINE                # printExpr
    |   ID '=' expr NEWLINE         # assign
    |   NEWLINE                     # blank
    ;

expr: DIFF expr                     # neg
    | expr FACT                     # fact
    | LPAR expr RPAR                # par
    | expr (MUL | DIV) expr         # mulDiv
    | expr ( ADD | DIFF ) expr      # addDiff
    | FMAX LPAR expr_list RPAR         # max
    | FSUM LPAR expr_list RPAR         # sum
    | FABS LPAR expr RPAR           # abs
    | INT                           # int
    | ID                            # id
    ;
    
expr_list 
    : (expr COMMA)* expr                
    ;

MUL : '*' ;
DIV : '/' ;
DIFF : '-' ;
ADD : '+' ;
FACT : '!';

FABS : 'abs' ;
FSUM : 'sum' ;
FMAX : 'max' ;
COMMA : ',' ;

LPAR : '(' ;
RPAR : ')' ;


ID  :   [a-zA-Z]+ ;      // match identifiers
INT :   [0-9]+ ;         // match integers
NEWLINE:'\r'? '\n' ;     // return newlines to parser (is end-statement signal)
WS  :   [ \t]+ -> skip ; // toss out whitespace
