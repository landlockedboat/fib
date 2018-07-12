#ifndef UTILS_HH_INCLUDED
#define UTILS_HH_INCLUDED

#include <iostream>
#include <fstream>
#include <sstream>      // std::stringstream
#include <string>
#include <vector>
#include <stdlib.h>

using namespace std;

void printError(string funcionName, string firstMessage,
  string object="", string secondMessage="");

void printLine(string message);

void printFile(string filePath);

string getFileAsString(string filePath);

string itoa(int i);
string ftoa(float f);

string generateOutputFilePath(string inputFilePath,
  string outputPath);

vector<string> getFolderFileNames(string path);

istream& getNum (istream& is, int& num);
istream& getBool (istream& is, bool& boo);

vector<char> toCharVector(string s);

vector<int> toIntVector(string s);

vector<string> toStringVector(int size, char *c[]);

string toOrdinal(int i);

#endif
