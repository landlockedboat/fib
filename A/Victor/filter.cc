#include "filter.hh"

char filterChar(char c)
{
  // If we've found a char that is not a
  // lowercase letter ...
  if(c < 'a' || c > 'z'){
    if(c >= '0' && c <= '9')
      return c;
    else if(c >= 'A' && c <= 'Z'){
      // We transform it to lowercase if it is uppercase
      return c += 'a' - 'A';
    }
    else if(c == '\n' || c == ' '){
      // If it is a newline character or a space, we transform
      // it to a space
      return ' ';
    }

    else{
      // Return a null char if it does not match anything
      return '\0';
    }

  }
  // If it's a valid char, we return it.
  return c;
}

string filterLine(string line)
{
  string filteredLine = "";
  for ( int i = 0 ; i < line.length(); i++)
  {
    // We add each filtered char of line to
    // filterdLine
    filteredLine += filterChar(line[i]);
  }
  // We add a space because we eliminate the newline char
  // when separating the input with lines. According
  // to out criteria, all newline chars must be converted
  // to whitespace, hence the addition.
  filteredLine += " ";
  return filteredLine;
}
  //Returns the path to the filtered file
string filterFile(string filePath,
  string outputPath, bool verbose){
  string line;
  ifstream input(filePath.c_str());
  string outputFilePath = "";
  if(input.is_open()){
    if(verbose)
    cout << "Reading from " << filePath << "..." << endl;
    ofstream output;
    outputFilePath = generateOutputFilePath(filePath, outputPath);
    output.open(outputFilePath.c_str());
    if(output.is_open()){
      if(verbose)
      cout << "Outputting to " << outputFilePath << "..." << endl;
      while ( getline (input, line) )
       {
         output << filterLine(line);
       }
    }
    else{
      cout << "Failed to output to " + outputFilePath + " aborting..." << endl;
    }
    input.close();
  }
  else {
    cout << "Unable to read from file " << filePath << "." << endl;
  }
  return outputFilePath;
}
