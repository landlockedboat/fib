#include <utility>
#include <set>
#include <map>

using namespace std;

typedef pair<int,int> SetPair;

//Inserts the suitable pairs in s according to m
//Duplicated pairs are treated
void putInResult(set<SetPair>& s, const map<vector<int>,vector<int> >& m) {
    for (map<vector<int>,vector<int> >::const_iterator it = m.begin(); it != m.end(); ++it) {
        vector<int> mySets = it->second;
        for (int i = 0; i < mySets.size(); ++i) {
          for (int j = i+1; j < mySets.size(); ++j) {
              SetPair p(mySets[i], mySets[j]);
              SetPair p2(p.second,p.first);
              if (s.find(p2) == s.end()) {//we only insert if the swapped is not
                s.insert(p);  
              }
          }
        }
    }
}


//Recieves a minHash and a threshold (thold) and return
//al the SetPairs that are supposed to be the same or more
//related; this is: pairs A B so that Jsim(A,B) >= thold
//This is roughly
set<SetPair> LSH(const vector<vector<unsigned int> >& minHash, double thold) {
    if (minHash.size() < 2)
        merror("LSH: don't make sense that size of minHash");
    int n = minHash[0].size();
    //size is supposed to be the same in all sets
    double r = getSuitableNRows(double(n), thold);
    int NRows = r;
    double b = double(n)/double (NRows);
    int NBands = int(b) + 1;
    set<SetPair> retVal;
    map<vector<int>,vector<int> > buckets;
    //We'll use the same map for each band, but we'll clear it
    //The key of each map is the vector of size NRows
    //For each key there is the index of all sets found
    
    for (int b = 0; b < NBands; ++b) { //for each band
        buckets.clear();
        for (int s = 0; s < minHash.size(); ++s) { //for each set
            vector<int> key;
            vector<unsigned int>::const_iterator first = minHash[s].begin() + b*NRows;
            vector<unsigned int>::const_iterator last;
            if (b != NBands - 1) { //the whole row
                last = first + NRows;
            }
            else {//
                last = minHash[s].end();
            }
            key = vector<int>(first, last);
            map<vector<int>,vector<int> >::iterator elem = buckets.find(key);
                if (elem != buckets.end()) {//it was already included
                    pair<vector<int>, vector<int> > old = *elem;
                    buckets.erase(elem);
                    old.second.push_back(s);
                    buckets.insert(old);
                }
                else {//firts time we include it
                    vector<int> actSet = vector<int> (1);
                    actSet[0] = s;
                    pair<vector<int>, vector<int> > mp(key,actSet);
                    buckets.insert(mp);
                }
            }
            putInResult(retVal,buckets);
        }
        return retVal;
    }
//}