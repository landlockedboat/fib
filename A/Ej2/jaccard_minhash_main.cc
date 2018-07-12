#include "../Victor/jaccard_minhash.hh"
#include "../Victor/utils.hh"

using namespace std;
int main(){
  bool isRunning = true;
  while(isRunning){
    printLine("Introduce the first signature: ");
    string s1;
    getline(cin, s1);
    printLine("Introduce the second signature: ");
    string s2;
    getline(cin, s2);
    vector<int> sig1 = toIntVector(s1);
    vector<int> sig2 = toIntVector(s2);
    float jacc = computeJaccardHashSignature(sig1, sig2);
    if(jacc < 0)
      printLine("Comparison failed! Do your signatures have the same length?");
    else
      cout << "Similarity between \"" << s1 << "\" and \"" <<
      s2 << "\" : " << jacc << endl;
    printLine("Do you want to run another comparison? (y / n)");
    getBool(cin, isRunning);
  }
}
