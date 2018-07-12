:- use_module(library(clpfd)).

main:-
  Vars = [R, J, P],
  Vars ins 1 .. 10,
  R #>= 3,
  J #>= 2 * R,
  P #>= 3 + J,
  labeling(Vars),
  nl, write(Vars), nl,nl,
  fail.

main.
