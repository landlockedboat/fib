#ifndef PERMGEN_HH_INCLUDED
#define PERMGEN_HH_INCLUDED

#include <iostream>
#include <algorithm>    // std::random_shuffle
#include <vector>       // std::vector
#include <cstdlib>      // std::rand, std::srand
#include <stdio.h>
#include <stdlib.h>
#include <map>
#include <fstream>
#include <string>
#include "utils.hh" // itoa, generateOutputFilePath
using namespace std;

vector<string> generateNPermutations(string filePath,
  string outputPath, int n, int seed,
  bool verbose);


#endif
