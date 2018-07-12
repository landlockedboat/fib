#define IMPORTS_PATH imports/

#include "imports/MinHashMat.hh"
#include "imports/ShingleSetMaker.cc"
#include "imports/jaccard_minhash.hh"
#include "imports/utils.hh"
#define FLT_TXT_PATH "../datasets/filteredtext/"

bool VERBOSE_FLAG;

void printUsage(){
	printLine("Usage: ");
	printLine("./minhash [-h] [-v]");
}

void evalParams(vector <string> commands){
	for (int i = 0; i < commands.size(); i++) {
		if(i == 0){
			// This is very much expected to happen
		}
		else if(commands[i] == "-v")
		{
			VERBOSE_FLAG = true;
		}
		else if(commands[i] == "-h")
		{
			printUsage();
			exit(0);
		}
		else
		{
			printLine("Unrecognised parameter: " + commands[i]);
			printUsage();
			exit(0);
		}
	}
}

int main(int argc, char *argv[]) {
	evalParams(toStringVector(argc, argv));
	printLine("Would you kindly introduce the number of files you wish to work with?");
	int numDocs;
	getNum(cin, numDocs);
	printLine("Introduce the shingle size:");
	int k;
	getNum(cin, k);
	vector<set<string> > shinglesSetVector(numDocs);

	for(int i = 0; i < numDocs; ++i) {
		printLine("Please introduce the path to the " + toOrdinal(i + 1) + " file you want to work with:");
		printLine("(Note that the file has to exist in datasets/filteredText)");
		string nthFilePath;
		getline(cin, nthFilePath);
		nthFilePath = FLT_TXT_PATH + nthFilePath;
		string docu = getFileAsString(nthFilePath);
		set<string> set = createSet(docu, k);

		if(VERBOSE_FLAG){
			printLine("Printing " + toOrdinal(i + 1) + " set: ");
			std::set<string>::iterator it;
			for(it = set.begin(); it != set.end(); ++it) {
					cout << *it << " ";
			}
			printLine("");
		}
		shinglesSetVector[i] = set;
	}
	int numHashFunc;
	printLine("Introduce the desired number of hash functions to work with");
	getNum(cin, numHashFunc);
	if(VERBOSE_FLAG)
		printLine("Creating CharMat...");
	CharMat charMat(shinglesSetVector);

	if(VERBOSE_FLAG){
		printLine("The Characteristic Matrix looks like this:");
		vector<set<int> > CM = charMat.get_CM();
		for(int i = 0; i < CM.size(); ++i) {
			printLine("S " + itoa(i+1));
			set<int>::iterator it;
			for(it = CM[i].begin(); it != CM[i].end(); ++it) {
				cout << *it << " ";
			}
			printLine("");
		}
	}
	if(VERBOSE_FLAG)
		printLine("Creating MinHashMat...");
	MinHashMat minHashMat(charMat,numHashFunc);
	vector<pair<int,int> > hashFunctions =
	minHashMat.get_funcs();

	if(VERBOSE_FLAG){
		printLine("The generated hash functions are the following:");
		for(int i = 0; i < numHashFunc; ++i) {
			printLine(itoa(hashFunctions[i].first) + "x + " + itoa(hashFunctions[i].second) + " mod(" + itoa(minHashMat.get_mprim()) + ")");
		}
	}
	vector<vector<int> > minHashMatrix =
	minHashMat.get_MHM();

	if(VERBOSE_FLAG){
		printLine("The corresponding minhash matrix is:");
		cout << "    ";
		for(int i = 0; i < numHashFunc; ++i) {
			cout << "h" << i+1 << " ";
		}
		cout << endl;
		for(int i = 0; i < minHashMatrix.size(); ++i) {
			cout << "S" << i+1 << ": ";
			for(int j = 0; j < minHashMatrix[0].size(); ++j) {
				cout << minHashMatrix[i][j] << "  ";
			}
			cout << endl;
		}
	}

	printLine("Computing Jaccard sim..");

	for (int i = 0; i < minHashMatrix.size(); i++) {
		for (int j = i + 1; j < minHashMatrix.size(); j++) {
			printLine("Jaccard similarity of the " + toOrdinal(i + 1) + " and the " + toOrdinal(j + 1) + " sets:");
			cout <<
			computeJaccardHashSignature(minHashMatrix[i],
			minHashMatrix[j]) << endl;
		}
	}
}
