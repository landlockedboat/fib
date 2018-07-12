grammar Calc;

prog:   stat+ EOF ; 

stat:   expr NEWLINE                
    |   ID '=' expr NEWLINE        
    |   NEWLINE                   
    ;

expr: DIFF expr
    | LPAR expr RPAR
    | expr (MUL | DIV) expr   
    | expr ( ADD | DIFF ) expr
    | (FMAX | FSUM) LPAR e_list RPAR
    | FABS LPAR expr RPAR
    | expr FACT
    | INT               
    | ID                    
    ;
    
e_list 
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
