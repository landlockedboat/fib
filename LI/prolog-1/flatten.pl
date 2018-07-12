flatten([], []).
flatten([A|B], [A|C]) :-
  atomic(A),
  flatten(B, C),!.

flatten([A|B], Z) :-
  flatten(A, L1),
  flatten(B, L2),
  append(L1, L2, Z).
