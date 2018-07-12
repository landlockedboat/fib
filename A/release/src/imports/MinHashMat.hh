#ifndef MINMAT_HH
#define MINMAT_HH

#include <iostream>
#include <set>
#include <stdlib.h>
#include <time.h>
#include <utility>
#include <vector>
#include "CharMat.hh"
using namespace std;

class MinHashMat{
private:
  // Vector of vectors representing the MinHash Matrix.
  // The MinHash Matrix is repesented by a group of sets, being each 
  // set repesented by a vector of integers.
  // Each integer of that vector represents a different hash function 
  // value for that set.
  // Thus, rows of the vector are sets, and columns different hash
  // functions.
  vector<vector<int> > MHM;
  // Vector containing hash functions, in order to use them for the
  // computation of the MinHash Matrix.
  // In the vector we have a pair that corresponds the first to a and
  // the second to b, in a function such as a*x + b mod(n)
  vector<pair<int,int> > funcs;
  // Integer containing the first prime number after the number of
  // rows of the Characteristic Matrix.
  int m;
  // Returns true if n is prime, false otherwise
  bool es_primer(int n);
  // If n is prime returns n, otherwise returns the closest next one.
  int prim(int n);
public:
  // Constructor function that receives a Characteristic Matrix, and 
  // the number of hash funcionts desired to do the MinHash.
  // It generates a MinHash Matrix starting with the given
  // Characteristic Matrix, the result will be like MHM matrix.
  MinHashMat(CharMat& c, int nf);
  // Function that returns the MinHash Matrix.
  vector<vector<int> > get_MHM();
  // Function that returns the funcs vector.
  vector<pair<int,int> > get_funcs();
  // Function that returns the first prime number after the number of
  // rows of the Characteristic Matrix.
  int get_mprim();
};

#endif
