% num_casa  ->  1..5
% color     ->  A1..5
% profesion ->  B1..5
% animal    ->  C1..5
% bebida    ->  D1..5
% pais      ->  E1..5

incr(N, N1) :-
  N1 is N + 1.

decr(N, N1) :-
  N1 is N - 1.

side(N, N1) :-
  decr(N, N1).

side(N, N1) :-
  incr(N, N1).

casas:-	
  Sol = [
    [1,A1,B1,C1,D1,E1],
    [2,A2,B2,C2,D2,E2],
    [3,A3,B3,C3,D3,E3],
    [4,A4,B4,C4,D4,E4],
    [5,A5,B5,C5,D5,E5]
  ],
% member( [ n, c, p, a, b, p ]  , Sol),
  member( [ _, "roja", _, _, _, "Perú" ]  , Sol),
  member( [ _, _, _, "perro", _, "Francia" ]  , Sol),
  member( [ _, _, "pintor", _, _, "Japón" ]  , Sol),
  member( [ _, _, _, _, "ron", "China" ]  , Sol),
  member( [ 1, _, _, _, _, "Hungría" ]  , Sol),
  member( [ _, "verde", _, _, "coñac", _ ]  , Sol),
  member( [ N1, "verde", _, _, _, _ ]  , Sol),
  incr(N1, N11),
  member( [ N11, "blanca", _, _, _, _ ]  , Sol),
  member( [ _, _, "escultor", "caracol", _, _ ]  , Sol),
  member( [ _, "amarilla", "actor", _, _, _ ]  , Sol),
  member( [ 3, _, _, _, "cava", _ ]  , Sol),
  member( [ N2, _, "actor", _, _, _ ]  , Sol),
  side(N2, N22),
  member( [ N22, _, _, "caballo", _, _ ]  , Sol),
  member( [ N3, "azul", _, _, _, _ ]  , Sol),
  side(N3, N33),
  member( [ N33, _, _, _,_, "Hungría" ]  , Sol),
  member( [ _, _, "notario", _, "whisky", _ ]  , Sol),
  member( [ N4, _, "medico", _, _, _ ]  , Sol),
  side(N4, N44),
  member( [ N44, _, _, "ardilla", _, _ ]  , Sol),

  write(Sol), nl.
