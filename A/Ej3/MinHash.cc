#include "MinHash.hh"

void MinHash::fst_hash(const vector<set<string> >& Cjt_Sets) {
	std::tr1::hash<string> str_hash;
	for(int i = 0; i < Cjt_Sets.size(); ++i) {
		set<string>::iterator it;
		for(it = Cjt_Sets[i].begin(); it != Cjt_Sets[i].end(); ++it) {
			unsigned int a = str_hash(*it);
			CM[i].insert(a);
		}
	}
	for(int i = 0; i < MH.size(); ++i) {
		set<unsigned int>::iterator it = CM[i].begin();
		MH[i][0] = *it;
	}
}

void MinHash::gen_xor_funcs(int seed) {
// 	srand(time(NULL));
        srand(seed);
	for(int i = 0; i < funcs.size(); ++i) {
		funcs[i] = rand()%4294967295;
	}
}

MinHash::MinHash(const vector<set<string> >& Cjt_Sets, int t, int seed) {
	vector<vector<unsigned int> > tMH(Cjt_Sets.size(), vector<unsigned int>(t,-1));
	vector<set<unsigned int> > tCM(Cjt_Sets.size());
	vector<unsigned int> tfuncs(t-1);
	MH = tMH;
	CM = tCM;
	funcs = tfuncs;
	fst_hash(Cjt_Sets);
	gen_xor_funcs(seed);
	for(int i = 0; i < MH.size(); ++i) {
		set<unsigned int>::iterator it;
		for(it = CM[i].begin(); it != CM[i].end(); ++it) {
			for(int j = 0; j < funcs.size(); ++j) {
				unsigned int act = funcs[j] xor *it;
				if(act < MH[i][j+1] or MH[i][j+1] == -1) {
					MH[i][j+1] = act;
				}
			} 
		}
	}
}

vector<vector<unsigned int> > MinHash::get_MH() {
	return MH;
}

vector<set<unsigned int> > MinHash::get_CM() {
	return CM;
}

vector<unsigned int> MinHash::get_funcs(){
	return funcs;
}
