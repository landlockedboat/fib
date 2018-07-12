#include "../imports/utils.hh"
#include "../imports/filter.hh"

using namespace std;

bool VERBOSE_FLAG = false;

string INPUT_PATH;
string OUTPUT_PATH;

void printUsage(){
	printLine("Usage: ");
	printLine("./filter.out inputFolder outputFolder [-h] [-v]");
}

void evalParams(vector <string> commands){
	for (int i = 0; i < commands.size(); i++) {
		if(i == 0){
      // This is very much expected :)
    }
    else if(commands[i] == "-h")
    {
      printUsage();
      exit(0);
    }
    else if(i == 1){
      INPUT_PATH = commands[i];
      if(INPUT_PATH[INPUT_PATH.length() - 1] !=
      '/')
        INPUT_PATH += '/';
    }
    else if(i == 2){
      OUTPUT_PATH = commands[i];
      if(OUTPUT_PATH[OUTPUT_PATH.length() - 1] !=
      '/')
        OUTPUT_PATH += '/';
    }
    else if(commands[i] == "-v"){
      VERBOSE_FLAG = true;
    }
		else
		{
			printLine("Unrecognised parameter: " + commands[i]);
			printUsage();
			exit(0);
		}
	}
}

int main(int argc, char *argv[])
{
  if(argc < 3){
    printUsage();
    exit(0);
  }
  evalParams(toStringVector(argc, argv));
  vector<string> fileNames =
    getFolderFileNames(INPUT_PATH);
  for (int i = 0; i < fileNames.size(); i++) {
    string filePath = fileNames[i];
    filePath = INPUT_PATH + filePath;
    filterFile(filePath, OUTPUT_PATH, VERBOSE_FLAG);
  }
}
