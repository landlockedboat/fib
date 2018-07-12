% Change this for different inputs
:-include(entradaPacking1).

:-dynamic(varNumber/3).
% set to 1 to see symbolic output only; 0 otherwise.
symbolicOutput(0). 

% Some helpful definitions to make the code cleaner:
rect(B):-rect(B,_,_).
xCoord(X) :- width(W),  between(1,W,X).
yCoord(Y) :- height(H), between(1,H,Y).
width(B,W):- rect(B,W,_).
height(B,H):- rect(B,_,H).
insideTable(X,Y):- width(W), height(H), between(1,W,X), between(1,H,Y).

% All code above this line was given to me by the professor
% It consists of a series of functions and definitions that help
% and inspires me to solve this problem.

% ============================================================
% ============================================================

% The code i've written starts here

% Clause written by me
% InsideRect is a clause that defines all X and Y coordinates inside
% a rectangle that starts at (Sx, Sy) and ends at (Ex, Ey).
insideRect(Sx, Sy, Ex, Ey, X, Y):-
  between(Ex, Sx, X),
  between(Ey, Sy, Y).

% Variables: They might be useful
% starts-B-X-Y:   box B has its left-bottom cell with upper-right coordinates (X,Y)
%  fills-B-X-Y:   box B fills cell with upper-right coordinates (X,Y)

% ==============================
%   WRITE CLAUSES FUNCTIONS
%   START
% ==============================
writeClauses:-
  defineStart,
  cannotStartOutside,
  eachFilledXYatMostOneB, 
  eachBexactlyOneStart,
  true.

defineStart:-
  rect(B),
  xCoord(Sx),
  yCoord(Sy),
  width(B, W),
  Ex is Sx - W + 1,
  height(B, H),
  Ey is Sy - H + 1,
  insideTable(Ex, Ey),
  findall( fills-B-X-Y, insideRect(Sx, Sy, Ex, Ey, X, Y), Lits ),
  member(Lit, Lits),
  % Not start or fill, the same as
  % start -> fill
  writeClause( [\+starts-B-Sx-Sy, Lit] ),
  fail.
defineStart.

cannotStartOutside:-
  rect(B),
  xCoord(Sx),
  yCoord(Sy),
  width(B, W),
  Ex is Sx - W + 1,
  height(B, H),
  Ey is Sy - H + 1,
  \+insideTable(Ex, Ey),
  writeClause( [\+starts-B-Sx-Sy] ),
  fail.
cannotStartOutside.

eachFilledXYatMostOneB:- 
  xCoord(X),
  yCoord(Y),
  findall( fills-B-X-Y, rect(B), Lits ),
  atMost(1, Lits), 
  fail.
eachFilledXYatMostOneB.

eachBexactlyOneStart:-
  rect(B),
  findall( starts-B-X-Y, insideTable(X, Y), Lits ),
  exactly(1,Lits), 
  fail.
eachBexactlyOneStart.
% ==============================
%   WRITE CLAUSES FUNCTIONS
%   END
% ==============================

% Show the solution. Here M contains the literals that are true in the model:
displaySol(M):- 
  yCoord(Y), nl,
  xCoord(X),
  rect(B),
  member(fills-B-X-Y, M),
  write(B), 
  write(' '),
  fail.
displaySol(_).

% ============================================================
% ============================================================

% The code below was provided to me by the professor
% It consists on a collection of functions that help the developer write a
% CNF clauses for SAT in prolog.

% Express that Var is equivalent to the disjunction of Lits:
expressOr( Var, Lits ):- 
  member(Lit,Lits), negate(Lit,NLit), 
  writeClause([ NLit, Var ]), fail.
expressOr( Var, Lits ):- negate(Var,NVar), writeClause([ NVar | Lits ]),!.

% Cardinality constraints on arbitrary sets of literals Lits:
exactly(K,Lits):- atLeast(K,Lits), atMost(K,Lits),!.

% l1+...+ln <= k:  in all subsets of size k+1, at least one is false:
atMost(K,Lits):-   
  negateAll(Lits,NLits),
  K1 is K+1,    subsetOfSize(K1,NLits,Clause), writeClause(Clause),fail.
atMost(_,_).

% l1+...+ln >= k: in all subsets of size n-k+1, at least one is true:
atLeast(K,Lits):-  
  length(Lits,N),
  K1 is N-K+1,  subsetOfSize(K1, Lits,Clause), writeClause(Clause),fail.
atLeast(_,_).

negateAll( [], [] ).
negateAll( [Lit|Lits], [NLit|NLits] ):- negate(Lit,NLit), negateAll( Lits, NLits ),!.

negate(\+Lit,  Lit):-!.
negate(  Lit,\+Lit):-!.

subsetOfSize(0,_,[]):-!.
subsetOfSize(N,[X|L],[X|S]):-
  N1 is N-1, length(L,Leng), Leng>=N1, subsetOfSize(N1,L,S).
subsetOfSize(N,[_|L],   S ):- 
  length(L,Leng), Leng>=N,  subsetOfSize( N,L,S).


% main:

% print the clauses in symbolic form and halt
main:-  symbolicOutput(1), !, writeClauses, halt.   

main:-  
  initClauseGeneration,
  % generate the (numeric) SAT clauses and call the solver
  tell(clauses), writeClauses, told,          
  tell(header),  writeHeader,  told,
  numVars(N), numClauses(C),
  write('Generated '), write(C), 
  write(' clauses over '), write(N), write(' variables. '),nl,
  shell('cat header clauses > infile.cnf',_),
  write('Calling solver....'), nl,
  % if sat: Result=10; if unsat: Result=20.
  shell('picosat -v -o model infile.cnf', Result),  
  treatResult(Result),!.

treatResult(20):- write('Unsatisfiable'), nl, halt.
treatResult(10):- 
  write('Solution found: '), nl, see(model), symbolicModel(M), seen,
  displaySol(M), nl,nl,halt.

% initialize all info about variables and clauses:
initClauseGeneration:-  
  retractall(numClauses(   _)),
  retractall(numVars(      _)),
  retractall(varNumber(_,_,_)),
  assert(numClauses( 0 )),
  assert(numVars(    0 )),     !.

writeClause([]):- symbolicOutput(1),!, nl.
writeClause([]):- countClause, write(0), nl.
writeClause([Lit|C]):- w(Lit), writeClause(C),!.
w( Lit ):- symbolicOutput(1), write(Lit), write(' '),!.
w(\+Var):- var2num(Var,N), write(-), write(N), write(' '),!.
w(  Var):- var2num(Var,N),           write(N), write(' '),!.


% given the symbolic variable V, find its variable number N in the SAT solver:
var2num(V,N):- hash_term(V,Key), existsOrCreate(V,Key,N),!.
% V already existed with num N
existsOrCreate(V,Key,N):- varNumber(Key,V,N),!.                            
% otherwise, introduce new N for V
existsOrCreate(V,Key,N):- newVarNumber(N), assert(varNumber(Key,V,N)), !.  

writeHeader:- numVars(N),numClauses(C), write('p cnf '),write(N), write(' '),write(C),nl.

countClause:-     retract( numClauses(N0) ), N is N0+1, assert( numClauses(N) ),!.
newVarNumber(N):- retract( numVars(   N0) ), N is N0+1, assert(    numVars(N) ),!.

% Getting the symbolic model M from the output file:
symbolicModel(M):- 
  get_code(Char), readWord(Char,W), symbolicModel(M1),
  addIfPositiveInt(W,M1,M),!.
symbolicModel([]).
addIfPositiveInt(W,L,[Var|L]):- 
  W = [C|_], between(48,57,C), number_codes(N,W), N>0, varNumber(_,Var,N),!.
addIfPositiveInt(_,L,L).
% skip line starting w/ c
readWord( 99,W):- 
  repeat, get_code(Ch), member(Ch,[-1,10]), !, 
  get_code(Ch1), readWord(Ch1,W),!. 
% skip line starting w/ s
readWord(115,W):- 
  repeat, get_code(Ch), member(Ch,[-1,10]), !, 
  get_code(Ch1), readWord(Ch1,W),!. 
%end of file
readWord(-1,_):-!, fail. 
% newline or white space marks end of word
readWord(C,[]):- member(C,[10,32]), !. 
readWord(Char,[Char|W]):- get_code(Char1), readWord(Char1,W), !.
