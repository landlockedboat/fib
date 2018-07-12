#include "CharMat.hh"

CharMat::CharMat(vector<set<string> >& SetConj) {
	m = 0;
	vector<set<int> > tmp(SetConj.size());
	CM = tmp;
	map<string,int> dict;
	for(int i = 0; i < SetConj.size(); ++i) {
		set<int> act;
		for(set<string>::iterator it = SetConj[i].begin(); it != SetConj[i].end(); ++it) {
			map<string,int>::iterator it2;
			it2 = dict.find(*it);
			if(it2 == dict.end()) {
				dict[*it] = m;
				act.insert(m);
				++m;
			}
			else {
				act.insert(dict[*it]);
			}
		}
		CM[i] = act;
	}
}

int CharMat::get_m() {
	return m;
}

vector<set<int> > CharMat::get_CM() {
	return CM;
}
