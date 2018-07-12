#include "../imports/GeneralFunctions.cc"
#include "../imports/ShingleSetMaker.cc"
#include "../imports/Jaccard.hh"
#include <stdlib.h>
#include <set>
#include <iostream>
#include <fstream>

using namespace std;


typedef set<string> Set;
typedef pair<int,int> SetPair;


void Usage() {
  merror("jaccardPairs dir thold k");
}

set<SetPair> pairsOverThold(const vector<Set>& charmat, double thold) {
    set<SetPair> retVal;
    for (int i = 0; i < charmat.size(); ++i) {
      for (int j = i+1; j < charmat.size(); ++j) {
          double punt = JaccardSets(charmat[i], charmat[j]);
          if (punt >= thold) {
            SetPair sp(i,j);
            retVal.insert(sp);
          }
      }
    }
    return retVal;
}


//Hay que pasarle como parámetro el directorio
//con los ficheros, el threshold mínimo que se acepta
//y la k con la que se hacen los shingles
int main(int argc, char* argv[]) {
    if (argc != 4) Usage();
    string dir = argv[1];
    double thold = atof(argv[2]);
    int k = atoi(argv[3]);
    vector<string> nombres = getFileNamesFromDirPath(dir);
    vector<Set> charmat = vector<Set>(nombres.size());
    for (int i = 0; i < nombres.size(); ++i) {
        string path = dir + "/" + nombres[i];
        ifstream ifs(path.c_str());
        string s;
        getline(ifs, s);
        charmat[i] = createSet(s,k);
        ifs.close();
    }
    set<SetPair> pares = pairsOverThold(charmat, thold);
    cout << "The most similar pairs are: " << endl;
    for (set<SetPair>::const_iterator it = pares.begin(); it != pares.end(); ++it) {
      int prim = it->first;
      int sec = it->second;
      cout << nombres[prim] << " " << nombres[sec] << endl;
    }
}
