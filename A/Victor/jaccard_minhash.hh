#ifndef JACC_MIN_HH_INCLUDED
#define JACC_MIN_HH_INCLUDED

#include <vector>
using namespace std;

// The Jaccard similarity of two groups A B is defined by
// A n B / A u B. In this case, it is the ammount of elements
// on both vectors divided by its size.
// The signatures have to be of equal size for the function
// to return the similarity of them.
// Returns a float between 0 and 1 indicating the Jaccard
// similarity of two minhash signatures s1 and s2
// If the two signatues are invalid the function will
// return -1.0.
float computeJaccardHashSignature(vector<int> s1,
  vector<int> s2);

#endif
