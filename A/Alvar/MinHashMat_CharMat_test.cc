#include "MinHashMat.hh"

int main() {
	int numdocs;
	cout << "Introdueix el numero de documents a crear" << endl;
	cin >> numdocs;
	vector<set<string> > VS(numdocs);
	cout << "Introdueix un '-' seguit del llistat d'strings que composin els shingles de cadascun dels documents, tot acabant el llistat amn un altre '-'." << endl;
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
	cout << "..." << endl;
	cout << "-" << endl;
	string str;
	cin >> str;
	for(int i = 0; i < numdocs; ++i) {
		string s;
		while(cin >> s and s != "-") {
			VS[i].insert(s);
		}
	}
	int t;
	cout << "Introdueix el numero desitjat de funcions de hash per a la taula" << endl;
	cin >> t;
	CharMat cm(VS);
	cout << "La Characteristic Matrix te la seguent forma:" << endl;
	vector<set<int> > CM = cm.get_CM();
	for(int i = 0; i < CM.size(); ++i) {
		cout << "    S" << i+1 << "    " << endl;
		set<int>::iterator it;
		for(it = CM[i].begin(); it != CM[i].end(); ++it) {
			cout << *it << endl;
		}
	}
	cout << "Estem creant la matriu MinHash" << endl;
	MinHashMat m (cm,t);
	vector<pair<int,int> > f = m.get_funcs();
	cout << "Les funcions de hash generades son les seguents:" << endl;
	for(int i = 0; i < t; ++i) {
		cout << f[i].first << "x + " << f[i].second << " mod(" << m.get_mprim() << ")" << endl;
	}
	cout << "La taula MinHash corresponent es la seguent: " << endl;
	cout << "    ";
	for(int i = 0; i < t; ++i) {
		cout << "h" << i+1 << " ";
	}
	cout << endl;
	vector<vector<int> > MHM = m.get_MHM();
	for(int i = 0; i < MHM.size(); ++i) {
		cout << "S" << i+1 << ": ";
		for(int j = 0; j < MHM[0].size(); ++j) {
			cout << MHM[i][j] << "  ";
		}
		cout << endl;
	}
}
