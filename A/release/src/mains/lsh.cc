/*
 * Lo que tiene que hacer este programa es recibir el path de una carpeta que tiene
 * varios ficheros y un threshold t, y tiene que indicar, de alguna manera
 * qué pares de esos ficheros tienen una relación igual o superior a la indicada
 */
#include "../imports/GeneralFunctions.cc"
#include "../imports/ShingleSetMaker.cc"
#include "../imports/LSHmaker.cc"
#include "../imports/MinHash.hh"
#include <iostream>
#include <stdlib.h>
#include <set>
#include <vector>
#include <fstream>

typedef set<string> Set;
typedef pair<int,int> SetPair;

void Usage() {
  merror("LSH FolderPath thold nperm k seed");
}

void help() {
    cout << "De momento te aguantas" << endl;
    exit(0);
}


int main(int argc, char* argv[]) {
    if (argc != 6) Usage();
    //if (argv[2] == "--help") help();
    string dir = argv[1];
    double thold = atof(argv[2]);
    int nperm = atoi(argv[3]);
    int k = atoi(argv[4]);
    int seed = atoi(argv[5]);
    vector<string> nombres = getFileNamesFromDirPath(dir);
    vector<Set> charmat = vector<Set>(nombres.size());
    for (int i = 0; i < nombres.size(); ++i) {
        string path = dir + "/" + nombres[i];
        ifstream ifs(path.c_str());
        string s;
        getline(ifs, s);
//         cout << "Creando el set " << i << " con nombre " << nombres[i] << endl;
        charmat[i] = createSet(s,k);
//         cout << "El texto es" << endl;
//         cout << s << endl;
//         cout << "----------------------" << endl;

        ifs.close();
    }
    MinHash mh(charmat, nperm,seed);
    set<SetPair> pares = LSH(mh.get_MH(),thold);
    cout << "The most similar pairs are: " << endl;
    for (set<SetPair>::const_iterator it = pares.begin(); it != pares.end(); ++it) {
      int prim = it->first;
      int sec = it->second;
      cout << nombres[prim] << " " << nombres[sec] << endl;
    }
}
