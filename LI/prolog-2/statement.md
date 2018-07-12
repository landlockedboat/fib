 We are given a large rectangular piece of cloth from which we want
 to cut a set of smaller rectangular pieces. The goal of this problem
 is to decide how to cut those small pieces from the large cloth, i.e.
 how to place them. 

 Note 1: The smaller pieces cannot be rotated.
 
 Note 2: All dimensions are integer numbers and are given in
 meters. Additionally, the larger piece of cloth is divided into
 square cells of dimension 1m x 1m, and every small piece must
 obtained exactly by choosing some of these cells
 
 Extend this file to do this using a SAT solver, following the
 example of sudoku.pl:
 - implement writeClauses so that it computes the solution, and
 - implement displaySol so that it outputs the solution in the
   format shown in entradapacking5.pl.
