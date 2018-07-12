#ifndef BAG_HH
#define BAG_HH

#include <map>
#include <iostream>

using namespace std;

struct Bag {
    unsigned int tam;
    map<string,int> m;

    map<string,int> get_map() {
	  return m;
	}

    void clear() {
      tam = 1;
      m.clear();
    }

    unsigned int size() const {
      return tam;
    }

    bool empty() const {
      return not tam;
    }

    void insert(const string& s) {
      map<string,int>::iterator it = m.find(s);
      if (it == m.end()) {//es la primera vez
          m.insert(pair<string,int>(s,1));
      }
      else {
        pair<string,int> p = *it;
        m.erase(it);
        p.second += 1;
        m.insert(p);
      }
      ++tam;
    }

    bool contains(const string& s) const {
      return m.find(s) != m.end();
    }

    //Returns de number of times s is in the bag
    unsigned int numberTimes(const string& s) const {
        map<string,int>::const_iterator it = m.find(s);
        if (it == m.end()) return 0;
        return it->second;
    }

    //Returns an iterator to the first elements
    //or null if empty
    map<string,int>::const_iterator begin() const {
      return m.begin();
    }

    map<string,int>::const_iterator end() const {
      return m.end();
    }
};






#endif
