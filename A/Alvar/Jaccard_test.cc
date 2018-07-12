#include "Jaccard.hh"

int main() {
	char a;
	cout << "Escull si vols realitzar la similitud de Jaccard amb Sets introduint 's' o amb Bags introduint 'b'" << endl;
	cin >> a;
	if (a == 's') {
	set<string> VS1;
	set<string> VS2;
	cout << "Introdueix un '-' seguit del llistat d'strings que composin els shingles de cadascun dels 2 documents, tot acabant el llistat amb un altre '-'." << endl;
    cout << "Exemple: " << endl;
    cout << "-" << endl;
    cout << "shingle_1_doc1" << endl;
	cout << "..." << endl;
	cout << "shingle_n_doc1" << endl;
    cout << "-" << endl;
	cout << "shingle_1_doc2" << endl;
    cout << "..." << endl;
	cout << "shingle_n_doc2" << endl;
	cout << "-" << endl;
	string str;
	cin >> str;
	while(cin >> str and str != "-") {
	  VS1.insert(str);
	}
	while(cin >> str and str != "-") {
	  VS2.insert(str);
	}
	double res = JaccardSets(VS1,VS2);
	cout << "La similitud de Jaccard es: " << res << endl;
  }
  else if(a == 'b') {
	map<string,int> VS1;
	map<string,int> VS2;
	cout << "Introdueix un '-' seguit del llistat d'strings que composin els shingles de cadascun dels 2 documents, tot acabant el llistat amb un altre '-'." << endl;
    cout << "Exemple: " << endl;
    cout << "-" << endl;
    cout << "shingle_1_doc1" << endl;
	cout << "..." << endl;
	cout << "shingle_n_doc1" << endl;
    cout << "-" << endl;
	cout << "shingle_1_doc2" << endl;
    cout << "..." << endl;
	cout << "shingle_n_doc2" << endl;
	cout << "-" << endl;
	string str;
	cin >> str;
	while(cin >> str and str != "-") {
	  map<string,int>::iterator it = VS1.find(str);
	  if(it == VS1.end()) {
		  VS1[str] = 1;
	  }
	  else {
		  int num = it->second;
		  ++num;
		  VS1[str] = num;
	  }
	}
	while(cin >> str and str != "-") {
	  map<string,int>::iterator it = VS2.find(str);
	  if(it == VS2.end()) {
		  VS2[str] = 1;
	  }
	  else {
		  int num = it->second;
		  ++num;
		  VS2[str] = num;
	  }
	}
	double res = JaccardBags(VS1,VS2);
	cout << "La similitud de Jaccard es: " << res << endl;
  }
}
