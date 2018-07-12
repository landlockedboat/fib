/////////////////////////////////////
//Esta método recive una cadena de //
//chars ya filtrada y una k y      //
//retorna un set de strings, que   //
//son los shingles                 //
/////////////////////////////////////
#ifndef SHINGLE_SET_MAKER
#define SHINGLE_SET_MAKER



#include "GeneralFunctions.cc"
#include <set>
#include <string>

typedef set<string> Set;

Set createSet(const string& s, int k) {
    if (k > s.size()) merror("Create Set: Not suitable k to shingle");
    Set myset;
    string act = s.substr(0,k);
    myset.insert(act);
    for (int i = k; i < s.size(); ++i) {
      act.erase(0,1);
      act.push_back(s[i]);
      myset.insert(act);
    }
    return myset;
}


#endif