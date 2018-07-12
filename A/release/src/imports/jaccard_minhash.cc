#include "jaccard_minhash.hh"

float computeJaccardHashSignature(vector<int> s1,
  vector<int> s2){
    // If the signatures size is not equal
    // we return -1.0
    if(s1.size() != s2.size())
      return -1.0;
    // Assuming the two signatues of equal size,
    // if they are both empty we return 1.0 because
    // lambda = lambda
    if(s1.size() == 0)
      return 1.0;
    // In this variable we'll hold the ammount of
    // similar items found
    int similars;
    // We cache the size of the two signatures, assuming
    // equal sizes.
    int size = s1.size();
    // The total number of elements
    int totals = size * 2;
    for (int i = 0; i < size; i++) {
      if(s1[i] == s2[i])
        ++similars;
    }
    // The Jaccard similarity of two groups A B is defined by
    // A n B / A u B. This is the equivalent for char vectors
    return (float)similars / (float)totals;
}
