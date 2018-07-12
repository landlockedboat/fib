% Main program
programa(I) :-
  match(I, begin, I1),
  instr(I1, I2),
  match(I2, end, _).

match([H|T], H, T).

% Matching variables. I dont know if there's a better way to do this.
vari(I, O) :-
  match(I, x, O).

vari(I, O) :-
  match(I, y, O).

vari(I, O) :-
  match(I, z, O).

% instruction can be var = var + var
instruction(I, O):-
  vari(I, I1),
  match(I1, =,I2),
  vari(I2, I3),
  match(I3, +, I4),
  vari(I4, O).

% or an if statement
instruction(I, O):-
  match(I,  if, I1),
  vari(I1, I2),
  match(I2, =, I3),
  vari(I3, I4),
  match(I4, then, I5),
  instr(I5, I6),
  match(I6, else, I7),
  instr(I7, I8),
  match(I8, endif, O).

instr(I, O) :-
  instruction(I, O).

instr(I, O) :-
  instruction(I, I1),
  match(I1, ;, I2),
  instr(I2, O).
