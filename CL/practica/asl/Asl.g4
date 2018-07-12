grammar Asl;

program : function+ EOF
;

function
: FUNC ID '(' (param (COMMA param)*)? ')' (':' type)? declarations statements ENDFUNC
;

param
: ID ':' type
;

declarations
: (variable_decl)*
;

variable_decl
: VAR ID (COMMA ID)* ':' type
;

type
: INT
| FLOAT
| CHAR 
| BOOL
| ARRAY '[' arrsize ']' 'of' type
;

arrsize
: INTVAL
;


statements
: (statement)*
;

// The different types of instructions
statement
: left_expr ASSIGN expr ';'           # assignStmt
| IF expr THEN statements (ELSE statements)? ENDIF # ifStmt
| WHILE expr DO statements ENDWHILE   # whileStmt
| procedure ';' 											# procCall
| READ left_expr ';'                  # readStmt
| WRITE expr ';'                      # writeExpr
| WRITE STRING ';'                    # writeString
| RETURN ret_expr? ';'								# returnStmt
;

procedure
: ident '(' (expr (',' expr)*)? ')'
;

ret_expr
: expr
;

// Grammar for left expressions (l-values in C++)
left_expr
: ident
;

// Grammar for expressions with boolean, relational and aritmetic operators
expr    
: op=(SUB | ADD) expr                 # arithmetic
// Equal precedence than the previous one
| op=NOT expr                         # boolean
| expr op=(MUL | DIV | MOD) expr      # arithmetic
| expr op=(ADD | SUB) expr           	# arithmetic
| expr op=(GT | LT | EQ | NE | LE | GE) expr # relational
| expr op=AND expr                    # boolean
| expr op=OR  expr                    # boolean
| procedure			  										# exprProcCall
| '(' expr ')'                        # parenthesis
| INTVAL                              # value
| FLOATVAL                            # value
| CHARVAL															# value
| BOOLVAL															# value
| ident                               # exprIdent
;

ident
: ID ('[' expr ']')?
;

//////////////////////////////////////////////////
/// Lexer Rules
//////////////////////////////////////////////////

ASSIGN    : '=' ;
GT        : '>';
LT    		: '<';
EQ				: '==';
NE    		: '!=';
GE    		: '>=';
LE    		: '<=';
AND				: 'and';
OR				: 'or';
NOT				: 'not';

SUB      	: '-';
ADD      	: '+';
MUL       : '*';
DIV       : '/';
MOD       : '%';
VAR       : 'var';

INT       : 'int';
BOOL      : 'bool';
FLOAT     : 'float';
CHAR      : 'char';
ARRAY     : 'array';

IF        : 'if' ;
THEN      : 'then' ;
ELSE      : 'else' ;
ENDIF     : 'endif' ;

WHILE     : 'while' ;
DO      	: 'do' ;
ENDWHILE  : 'endwhile' ;

FUNC      : 'func' ;
RETURN		: 'return';
ENDFUNC   : 'endfunc' ;
READ      : 'read' ;
WRITE     : 'write' ;

INTVAL    : ('0'..'9')+ ;
FLOATVAL  : ('0'..'9')*'.'('0'..'9')+; 
CHARVAL   : '\'' ( ESC_SEQ | ~('\\'|'\'') )? '\'' ;
BOOLVAL   : ('true'|'false');

ID        : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;
COMMA			: ',';

// Strings (in quotes) with escape sequences
STRING    : '"' ( ESC_SEQ | ~('\\'|'"') )* '"' ;

fragment
ESC_SEQ   : '\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\') ;

// Comments (inline C++-style)
COMMENT   : '//' ~('\n'|'\r')* '\r'? '\n' -> skip ;

// White spaces
WS        : (' '|'\t'|'\r'|'\n')+ -> skip ;
// Alternative description
// WS        : [ \t\r\n]+ -> skip ;
