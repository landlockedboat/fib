#include "antlr4-runtime.h"
#include "AslLexer.h"
#include "AslParser.h"
#include "tree/ParseTreeWalker.h"

#include "../common/TypesMgr.h"
#include "../common/SymTable.h"
#include "../common/TreeDecoration.h"
#include "../common/SemErrors.h"
#include "SymbolsListener.h"
#include "TypeCheckListener.h"
#include "../common/code.h"
#include "CodeGenListener.h"

#include <iostream>
#include <fstream>    // ifstream

#include <cstdio>     // fopen
#include <cstdlib>    // EXIT_FAILURE, EXIT_SUCCESS

// using namespace std;
// using namespace antlr4;



int main(int argc, const char* argv[]) {
  // check the correct use of the program
  if (argc > 2) {
    std::cout << "Usage: ./main [<file>]" << std::endl;
    return EXIT_FAILURE;
  }
  if (argc == 2 and not std::fopen(argv[1], "r")) {
    std::cout << "No such file: " << argv[1] << std::endl;
    return EXIT_FAILURE;
  }

  // open input file (or std::cin) and create a character stream
  antlr4::ANTLRInputStream input;
  if (argc == 2) {  // reads from <file>
    std::ifstream stream;
    stream.open(argv[1]);
    input = antlr4::ANTLRInputStream(stream);
  }
  else {            // reads fron std::cin
    input = antlr4::ANTLRInputStream(std::cin);
  }

  // create a lexer that consumes the character stream and produce a token stream
  AslLexer lexer(&input);
  antlr4::CommonTokenStream tokens(&lexer);

  // create a parser that consumes the token stream, and parses it.
  AslParser parser(&tokens);

  // call the parser and get the parse tree
  antlr4::tree::ParseTree *tree = parser.program();

  // check for lexical or syntactical errors
  if (lexer.getNumberOfSyntaxErrors() > 0 or
      parser.getNumberOfSyntaxErrors() > 0) {
    std::cout << "Lexical and/or syntactical errors have been found." << std::endl;
    return EXIT_FAILURE;
  }

  // print the parse tree (for debugging purposes)
  // std::cout << tree->toStringTree(&parser) << std::endl;

  // create a walker that will traverse the tree and do several things,
  // like checking variable types or generating code.
  antlr4::tree::ParseTreeWalker walker;

  // Auxililary classes we are going to need to store information while
  // traversing the tree. They are described below in this document
  TypesMgr       types;
  SymTable       symbols(types);
  TreeDecoration decorations;
  SemErrors      errors;

  // Create a Listener that looks for variables and function declarations in the tree
  // and stores required information
  SymbolsListener symboldecl(types, symbols, decorations, errors);
  // Traverse the tree using this listener, to collect information about declared identifiers
  walker.walk(&symboldecl, tree);

  // Create another Listener that will perform type checkings wherever it is needed
  // (on expressions, assignments, parameter passing, etc)
  TypeCheckListener typecheck(types, symbols, decorations, errors);
  // Traverse the tree using this listener, so all types are checked
  walker.walk(&typecheck, tree);

  if (errors.getNumberOfSemanticErrors() > 0) {
    std::cout << "There are semantic errors: no code generated." << std::endl;
    return EXIT_FAILURE;
  }

  // Auxiliary class to store the code we will be creating
  code mycode;
  // Create a third listener that will generate code for each part of the tree
  CodeGenListener codegenerator(types, symbols, decorations, mycode);
  // Traverse the tree using this listener, so code is generated and stored in 'mycode'
  walker.walk(&codegenerator, tree);

  // print generated code as output
  std::cout << mycode.dump() << std::endl;

  return EXIT_SUCCESS;
}
