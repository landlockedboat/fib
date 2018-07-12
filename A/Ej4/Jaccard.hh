#ifndef JACCARD_HH
#define JACCARD_HH

#include <iostream>
#include <set>
#include <map>
#include <string>
#include <algorithm>
#include <tr1/functional>
#include <stdlib.h>
using namespace std;

// Function that converts a Set of strings to a Set of unsigned 
// integers.
set<unsigned int> SetStringToUInt(set<string> s);

// Function that converts a Map of strings and integers to a Set of 
// unsigned integers and integers.
map<unsigned int,int> MapStringToUInt(map<string,int> s);

// Pre: JaccardSets rep dos contenidors de tipus Set d'strings de
// qualsevol mida
// Post: Retorna un double (de 0 a 1) que fa referència a la Similitud 
// de Jaccard entre els dos sets de shingles donats
double JaccardSets (set<string> ps, set<string> ss);

// Pre: JaccardBags rep dos contenidors de tipus Map amb key string i
// value integer, tenint aquests qualsevol mida
// Post: Retorna un double (de 0 a 1/2) que fa referència a la Similitud 
// de Jaccard entre els dos bags de shingles donats
double JaccardBags (map<string,int> ps, map<string,int> ss);

#endif
