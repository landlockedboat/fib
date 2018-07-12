#include <iostream>
#include <string>
#include <fstream>
#include "ShingleSetMaker.cc"
#include "ShingleBagMaker.cc"
#include "Bag.h"
#include "../Alvar/Jaccard.hh"

using namespace std;

int main() {
	cout << "Write below the name of two documents, in order to compute their Jaccard Similarity." << endl;
	cout << "Separate each of the names with an endline." << endl;
	cout << "Make sure both documents are located at /datasets/filteredtext." << endl;
	string fitxer1, fitxer2;
	cin >> fitxer1;
	cin >> fitxer2;
	string dir = "../datasets/filteredtext/";
	string f1 = dir + fitxer1;
	string f2 = dir + fitxer2;
	ifstream file1(f1.c_str());
	ifstream file2(f2.c_str());
	if(!file1) {
	  cout << "Error: Unnable to open the file: " << fitxer1 << endl;
      return 1;
	}
	if(!file2) {
	  cout << "Error: Unnable to open the file: " << fitxer2 << endl;
      return 1;
	}
	string str1, str2;
	getline(file1, str1);
	getline(file2, str2);
	char a;
	cout << "Select whether you want to execute a Set or Bag Jaccard Similarity, inserting an 's' or a 'b' respectively" << endl;
	cin >> a;
	if(a == 's') {
      int k;
	  cout << "Insert the desired k to generate k-shingles from the documents." << endl;
	  cin >> k;
	  set<string> s1;
	  set<string> s2;
	  s1 = createSet(str1,k);
	  s2 = createSet(str2,k);
	  double res = JaccardSets(s1,s2);
	  cout << "The similarity between the selected documents is: " << res << endl;
	}
	else if(a == 'b') {
	  int k;
	  cout << "Insert the desired k to generate k-shingles from the documents." << endl;
	  cin >> k;
	  Bag b1, b2;
	  map<string,int> m1;
	  map<string,int> m2;
	  b1 = createBag(str1,k);
	  b2 = createBag(str2,k);
	  m1 = b1.get_map();
	  m2 = b2.get_map();
	  double res = JaccardBags(m1,m2);
	  cout << "The similarity between the selected documents is: " << res << endl;
	}
	else merror("Error: Not valid kind of Jaccard Similarity");
}
