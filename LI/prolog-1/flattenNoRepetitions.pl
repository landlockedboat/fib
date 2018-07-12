% I have to manually implement subset because it is not recursive in
% SWIPL
subsett([], []).
subsett([E|Tail], [E|NTail]):-
  subsett(Tail, NTail).
subsett([_|Tail], NTail):-
  subsett(Tail, NTail).

% Has this list repeated elements?
norep([]).
norep([L]) :-
  atomic(L),!.

norep([A|L]) :-
  % A should not be a member of L
  \+ member(A, L),
  norep(L),!.

% First list contains all elements of
% second list
containselems([], []).

containselems([A|B], L) :-
  member(A, L),
  subtract(L, [A], Z),
  containselems(B, Z).

% Is L1 a subset that contains all elements of L
% and has no repetitions?
setnorep(L, L1) :-
  subsett(L, L1),
  containselems(L1, L),
  norep(L1).

flattenNoRepetitions([], []).

flattenNoRepetitions([A|B], Z) :-
  atomic(A),
  flattenNoRepetitions(B, C),
  setnorep([A|C], Z),!.

flattenNoRepetitions([A|B], Z) :-
  flattenNoRepetitions(A, L1),
  flattenNoRepetitions(B, L2),
  append(L1, L2, C),
  setnorep(C, Z),!.
