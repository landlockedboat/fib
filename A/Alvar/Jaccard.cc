#include "Jaccard.hh"

set<unsigned int> SetStringToUInt(set<string> s) {
	std::tr1::hash<string> str_hash;
	set<unsigned int> s2;
	set<string>::iterator it;
	for(it = s.begin(); it != s.end(); ++it) {
		s2.insert(str_hash(*it));
	}
	return s2;
}

map<unsigned int,int> MapStringToUInt(map<string,int> s) {
	std::tr1::hash<string> str_hash;
	map<unsigned int,int> s2;
	map<string,int>::iterator it;
	for(it = s.begin(); it != s.end(); ++it) {
		unsigned int key = str_hash(it->first);
		s2[key] = it->second;
	}
	return s2;
}

double JaccardSets (set<string> ps, set<string> ss) {
  set<unsigned int> p = SetStringToUInt(ps);
  set<unsigned int> s = SetStringToUInt(ss);
  double interseccio = 0.0;
  int unio = p.size() + s.size();
  set<unsigned int>::iterator itp=p.begin();
  set<unsigned int>::iterator its=s.begin();
  while(itp != p.end() and its != s.end()) {
	if(*itp == *its) {
      ++interseccio;
	  ++itp;
	  ++its;
	}
	else if(*itp > *its) ++its;
	else ++itp;
  }
  unio -= interseccio;
  return (double)(interseccio/unio);
}

double JaccardBags (map<string,int> ps, map<string,int> ss) {
  map<unsigned int,int> p = MapStringToUInt(ps);
  map<unsigned int,int> s = MapStringToUInt(ss);
  double interseccio = 0.0;
  double unio = 0.0;
  map<unsigned int,int>::iterator itp=p.begin();
  map<unsigned int,int>::iterator its=s.begin();
  if(itp != p.end()) unio+=itp->second;
  if(its != s.end()) unio+=its->second;
  while(itp != p.end() and its != s.end()) {
	if(itp->first == its->first) {
      interseccio += min(itp->second,its->second);
	  ++itp;
	  ++its;
	  if(itp != p.end()) unio+=itp->second;
      if(its != s.end()) unio+=its->second;
	}
	else if(itp->first > its->first) {
	  ++its;
      if(its != s.end()) unio+=its->second;
	}
	else {
	  ++itp;
	  if(itp != p.end()) unio+=itp->second;
    }
  }
  return (double)(interseccio/unio);
}
