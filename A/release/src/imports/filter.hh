#ifndef FILTER_HH_INCLUDED
#define FILTER_HH_INCLUDED

#include <iostream>
#include <fstream>
#include <string>
#include "utils.hh"

using namespace std;

// Function that, given a valid file path filePath filters the
// contents of the file and outputs them to a file which
// path will be generateOutputFilePath(filePath).
// This function returns the path to the filtered file.
string filterFile(string filePath,
  string outputPath, bool verbose = false);

#endif
