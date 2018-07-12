#ifndef GENERAL_FUNCTIONS
#define GENERAL_FUNCTIONS

#include <stdlib.h>
#include <iostream>
#include <set>
#include "Bag.h"
#include <cmath>
#include <vector>
#include <stdio.h>

using namespace std;

typedef set<string> Set;

//Displays message m and exits de program
void merror(const string& m) {
    cout << m << endl;
    exit(10);
}

//If s is not empty, returns the 
//size of the shingles
//Otherwise returns 0
int getk(const Set& s) {
    if (s.empty()) return 0;
    return s.begin()->size();
}

int getk(const Bag& b) {
  if (b.empty()) return 0;
  return b.begin()->first.size();
}

int getHashResult(unsigned int x, unsigned int a, unsigned int b, unsigned int mod) {
    if (mod <= 0 or a < 0 or a >= mod or b < 0 or b >= mod or x < 0 or x > mod)
        merror("getHashResult: Not suitable values for a, b, mod or x");
    int retVal = x*a + b;
    retVal %= mod;
    return retVal;
}

//Returns de number of collisions produced applying the function 
// a*x + b % mod, when x is 0..(mod-1)

int evaluateHashFunction(unsigned int a, unsigned int b, unsigned int mod) {
    if (mod <= 0 or a < 0 or a >= mod or b < 0 or b >= mod)
        merror("evaluateHashFunction: Not suitable values for a, b or mod");
    int retVal = 0;
    vector<bool> been = vector<bool>(mod, false);
    for (int i = 0; i < mod; ++i) {
      int val = getHashResult(i,a,b,mod);
      if (been[val]) ++retVal;
      else been[val] = true;
    }
    return retVal;
}

//Returns the nuitable number of bands
//to produce an LSH with sim >= thold
//The number of permutations, this is, the
//size of the Sets signature, will have to 
//be: n = N_ROWS*b (b is return value)
// int  getNbands(double thold) {
//     if (thold <= 0 || 1 <= thold)
//         merror("getNbands: bad thold. Suitable: 0 < thold < 1");
//     //Basically want: b = 1/(thold)^N_ROWS
//     double b = 1.0/(pow(thold, double(N_ROWS)));
//     b = ceil(b); //Integral over it
//     int B = b;
//     return B;
// }


/////////////////////////////////////////////////
//Just getSuitableNRows needs to use this.
//Vey concrete calculation
/////////////////////////////////////////////////
double f(double x, double n, double t) {
    double retVal = pow(t, x);
    retVal *= n;
    retVal -= x;
    return retVal;
}
double df(double x, double n, double t) {

  double retVal = pow(t,x);
  retVal *= n;
  retVal *= log(t);
  retVal -= 1.0;
  return retVal;
}
/////////////////////////////////////////////////
////////////////////////////////////////////////

//Returns the suitable number of rows each band
//should have in LSH to get pairs with similarity
//>= thold
//n is the number of permutations done, and also
//the size of each minhash signature
double getSuitableNRows(double n, double thold) {
    double retVal1 = 5; //can be changed
    double retVal2;
    do {
        retVal2 = retVal1;
        retVal1 -= f(retVal1,n,thold)/df(retVal1,n,thold);
        
    } while (fabs(retVal1 - retVal2) > 0.05); //Depends on the precision required
    
    return retVal1;
}

//Returns the equivalent number of an
//string according to the k and our 
//aphabet, which is '\n' U ['a'..'z'],
//without ñ nor ç nor accents nor capital letters

// int stringToNum(const string& s) {
//     if (s.size() < 1) merror("stringToNum: not suitable string");
//     int k = 'z' - 'a' + 2;
//     int retVal = 0;
//     for (int i = s.size()-1; i >= 0; --i) {
//         retVal = k*retVal + charToNum(s[i]);
//     }
// }

//Recive el path de un directorio y retorna un vector con los nombres
//de todos los ficheros que hay dentro del directorio
vector<string> getFileNamesFromDirPath(const string& dirpath) {
    vector<string> retVal;
    FILE *file;
    string comando = "ls -1 " + dirpath + "/";
//     file = popen(&comando, "r");
    file = popen(comando.c_str(), "r");
    if (file) {
        int c;
        string s = "";
        while (char(c = fgetc(file)) != EOF) {
            char ch = char(c);
            if (ch != '\n') {
                s.push_back(ch);
            }
            else {
                retVal.push_back(s);
                s = "";
            }
        }
        return retVal;
    }
    else {
      merror("getFileNamesFromDirPath: problemas para abrir la carpeta");
    }
}



#endif