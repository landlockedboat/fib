#include "utils.hh"

void printError(string funcionName, string firstMessage,
  string object /*=""*/, string secondMessage /*=""*/)
{
  cout << "Error in " << funcionName << ":" << endl;
  cout << firstMessage << object << secondMessage << endl;
}

void printLine(string message){
  cout << message << endl;
}

void printFile(string filePath){
  string line;
  ifstream input(filePath.c_str());
  if(input.is_open())
  {
    while ( getline (input, line) )
     {
       cout << line;
     }
     cout << endl;
    input.close();
  }
  else
  {
    printError("printFile", "Unable to open file ", filePath);
  }

}

string getFileAsString(string filePath){
  string ret = "";
  ifstream input(filePath.c_str());
  if(input.is_open())
  {
    string line;
    while ( getline (input, line) )
     {
       ret += line;
     }
    input.close();
  }
  else
  {
    printError("getFileAsString", "Unable to open file ", filePath);
  }
  return ret;
}


string itoa(int i){
  stringstream ss;
  ss << i;
  return ss.str();
}

string ftoa(float f){
  stringstream ss;
  ss << f;
  return ss.str();
}

string generateOutputFilePath(string inputFilePath,
  string outputPath)
{
  string outputFileName = "";
  // We find the ".txt" substring inside the name
  int txt_pos = inputFilePath.find(".txt");
  // Because std::string::find returns string::npos (the last
  // element of the string)
  // if the element has not been found,
  // we can proceed without checking if we've found ".txt" in the name
  //Same thing, but in this case we do care if this has not been
  //found
  int dir_pos = inputFilePath.find("/");
  if(dir_pos == string::npos)
    dir_pos = 0;
  outputFileName = inputFilePath.substr (dir_pos + 1 ,
    txt_pos - dir_pos - 1);
  return outputPath + outputFileName + "_out.txt";
}

vector<string> getFolderFileNames(string path) {
    vector<string> retVal;
    FILE *file;
    string comando = "ls -1 " + path + "/";
    file = popen(comando.c_str(), "r");
    if (file) {
        int c;
        string s = "";
        while (char(c = fgetc(file)) != EOF) {
            char ch = char(c);
            if (ch != '\n') {
                s.push_back(ch);
            }
            else {
                retVal.push_back(s);
                s = "";
            }
        }
        return retVal;
    }
    else {
      printError("getFolderFileNames", "Cannot open file");
    }
}

istream& getNum (istream& is, int& num){
  string s;
  getline(is, s);
  num = atoi(s.c_str());
  return is;
}
istream& getBool (istream& is, bool& boo){
  string s;
  getline(is, s);
  if(s.length() == 0){
    boo = false;
    return is;
  }
  boo = s[0] == 'y' || s[0] == 'Y';
  return is;
}

vector<char> toCharVector(string s){
  int length = s.length();
  vector<char> ret(length);
  for (int i = 0; i < length; i++) {
    ret[i] = s[i];
  }
  return ret;
}

vector<int> toIntVector(string s){
  int length = s.length();
  vector<int> ret(length);
  for (int i = 0; i < length; i++) {
    ret[i] = s[i] - '0';
  }
  return ret;
}

vector<string> toStringVector(int size, char *c[]){
  vector<string> ret;
  for (int i = 0; i < size; i++) {
    string s(c[i]);
    ret.push_back(s);
  }
  return ret;
}


string toOrdinal(int i){
  string ret;
  ret = itoa(i);
  int last = ret.length() -1;
  if(ret[last] == '1'){
    return ret += "st";
  }
  else if(ret[last] == '2'){
    return ret += "nd";
  }
  else if(ret[last] == '3'){
    return ret += "rd";
  }
  else {
    return ret += "th";
  }
}
