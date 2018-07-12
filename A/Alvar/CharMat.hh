#ifndef CHARMAT_HH
#define CHARMAT_HH

#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <map>
using namespace std;

class CharMat{
private:
  // Vector of sets representing the characteristic matrix.
  // The characteristic matrix is repesented by a group of sets,
  // owning each set only its shingles, instead of a list of 0s and 1s.
  // Each shingle is repesented by an integer from 0 to the number
  // of different shingles that appears among all sets considered.
  vector<set<int> > CM;
  // Number of different shingles found among all sets or, what is the
  // same, the number of rows of the original characteristic matrix.
  int m;
public:
  // Constructor function that, from a group of sets containing 
  // shingles (each represented by a string), generates a 
  // Characteristic Matrix like CM, with the structure of a 
  // vector<set<int> >.
  CharMat(vector<set<string> >& SetConj);
  // Function that returns the amount of different shingles found
  // among all sets.
  int get_m();
  // Function that returns the Characteristic Matrix.
  vector<set<int> > get_CM();
};

#endif
