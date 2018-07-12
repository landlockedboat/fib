#include "permgen.hh"


string generateNthPermutation(vector<int> mapKeys,
  map<int, string> intWordsMap,
  string outputFilePath, bool verbose){
  //The file to which we'll output to
  ofstream output(outputFilePath.c_str());
  //We check if the file opened successfully
  if(output.is_open()){
    if(verbose)
    cout << "Outputting to " << outputFilePath << "..." << endl;
  }
  else{
      cout << "Failed to output to " + outputFilePath +
      " aborting..." << endl;
    return "";
  }
  random_shuffle ( mapKeys.begin(), mapKeys.end() );
  // Then, we output the map with the shuffled keys to the output file
  for (size_t i = 0; i < mapKeys.size(); i++) {
    output << intWordsMap[mapKeys[i]] << " ";
  }
  return outputFilePath;
}

string generateNthOutputFilePath(string outputFilePath, int n){
  int txt_pos = outputFilePath.find(".txt");
  return outputFilePath.substr(0, txt_pos) + "_" + itoa(n) + ".txt";
}

vector<string> generateNPermutations(string filePath,
  string outputPath, int n, int seed, bool verbose)
{
  //The input file from which we get the words to permutate
  ifstream input(filePath.c_str());
  vector<string> ret;
  if(input.is_open()){
    if(verbose)
    cout << "Reading from " << filePath << "..." << endl;
    // This is the map of words which will be shuffled using a vector
    // of its keys and the random_shuffle function on the vector.
    map <int, string> intWordsMap;
    int index = 0;
    string line;
    while ( getline (input, line) )
    {
      // We populate the map
      intWordsMap.insert(pair <int, string> (index, line));
      index++;
    }
    // Create and populate the keys vector
    vector <int> mapKeys(index);
    for (int i = 0; i < index; i++) {
      mapKeys[i] = i;
    }
    // Initialise randomizer
    srand ( unsigned ( seed ) );
    for (int i = 0; i < n; i++) {
      string outputFilePath = generateOutputFilePath(filePath,
        outputPath);
      outputFilePath = generateNthOutputFilePath(outputFilePath,
        i);
      ret.push_back(
        generateNthPermutation(mapKeys, intWordsMap, outputFilePath, verbose)
      );
    }
  }
  else {
    cout << "Unable to read from file " << filePath << "." << endl;
  }
  input.close();
  return ret;
}
