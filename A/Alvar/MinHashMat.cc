#include "MinHashMat.hh"

MinHashMat::MinHashMat(CharMat& c, int nf) {
	vector<set<int> > CM = c.get_CM();
	int nr = c.get_m();
	vector<vector<int> > MinHash(CM.size(), vector<int>(nf,-1));
	MHM = MinHash;
	vector<pair<int,int> > tfuncs(nf);
	funcs = tfuncs;
	srand(time(NULL));
	m = prim(nr);
	for(int i = 0; i < nf; ++i) {
		pair <int,int> hash;
		int a = 1+rand()%(m-1);
		int b = rand()%m;
		hash = make_pair(a,b);
		funcs[i] = hash;
	}
	for(int i = 0; i < nr; ++i) {
		for(int j = 0; j < CM.size(); ++j) {
			set<int>::iterator it;
			it = CM[j].find(i);
			if(it != CM[j].end()) {
			    for(int k = 0; k < nf; ++k) {
					int hr = (funcs[k].first * i + funcs[k].second)%m;
					if(hr < MHM[j][k] or MHM[j][k] == -1) {
						MHM[j][k] = hr;
					}
				}
			}
		}
	}
}

vector<vector<int> > MinHashMat::get_MHM() {
	return MHM;
}

vector<pair<int,int> > MinHashMat::get_funcs() {
	return funcs;
}

int MinHashMat::get_mprim() {
	return m;
}

bool MinHashMat::es_primer(int n) {
	if (n <= 3) return(n == 2 or n == 3);
	else if (n % 2 == 0 or n % 3 == 0) return false;
	else {
		for (int i = 5; i * i <= n; i = i + 6) {
			if (n % i == 0 or n % (i + 2) == 0) return false;
		}
		return true;
	}
}

int MinHashMat::prim(int n) {
  if(es_primer(n)) return n;
  else {
	  int i = n + 1;
	  while(not es_primer(i)) {
		  ++i;
	  }
	  return i;
  }
}
