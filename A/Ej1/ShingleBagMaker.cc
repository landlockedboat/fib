/////////////////////////////////////
//Esta método recive una cadena de //
//chars ya filtrada y una k y      //
//retorna un bag de strings, que   //
//son los shingles                 //
/////////////////////////////////////
#ifndef SHINGLE_BAG_MAKER
#define SHINGLE_BAG_MAKER


#include "GeneralFunctions.cc"
#include <string>
#include "Bag.h"

Bag createBag(const string& s, int k) {
    if (k > s.size()) merror("Createbag: Not suitable k to shingle");
    Bag mybag;
    mybag.clear();
    string act = s.substr(0,k);
    mybag.insert(act);
    for (int i = k; i < s.size(); ++i) {
      act.erase(0,1);
      act.push_back(s[i]);
      mybag.insert(act);
    }
    return mybag;
}




#endif
