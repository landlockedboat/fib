
#include "antlr4-runtime.h"
#include "ExprLexer.h"
#include "ExprParser.h"
#include "ExprBaseListener.h"
#include "tree/ParseTreeWalker.h"

#include <iostream>
#include <fstream>
#include <cstdio>    // fopen

// using namespace std
// using namespace antlr4;


//////////////////////////////////////////////////////////////////////
// Sample information "collector"
class NodeCounterListener : public ExprBaseListener {
public:
  int interior = 0;
  int leaves = 0;

  void enterEveryRule(antlr4::ParserRuleContext *ctx) {
    interior++;
  }

  void visitTerminal(antlr4::tree::TerminalNode *node) {
    leaves++;
  }
};
// Sample information "collector"
//////////////////////////////////////////////////////////////////////


int main(int argc, const char* argv[]) {
  // check the correct use of the program
  if (argc != 2) {
    std::cout << "Usage: ./main <file>" << std::endl;
    return EXIT_FAILURE;
  }
  if (not std::fopen(argv[1], "r")) {
    std::cout << "No such file: " << argv[1] << std::endl;
    return EXIT_FAILURE;
  }

  // open input file and create a character stream
  std::ifstream stream;
  stream.open(argv[1]);
  antlr4::ANTLRInputStream input(stream);

  // create a lexer that consumes the character stream and produce a token stream
  ExprLexer lexer(&input);
  antlr4::CommonTokenStream tokens(&lexer);

  // create a parser that consumes the token stream, and parses it.
  ExprParser parser(&tokens);

  // call the parser and get the parse tree
  antlr4::tree::ParseTree *tree = parser.s();

  // print the parse tree (for debugging purposes)
  std::cout << tree->toStringTree(&parser) << std::endl;

  ////////////////////////////////////////////////////////////////////
  
  // create a walker that will traverse the tree
  antlr4::tree::ParseTreeWalker walker;
  
  // Create a listener that counts interior and leave nodes
  NodeCounterListener counter;
  
  // Traverse the tree using this listener
  walker.walk(&counter, tree);

  // Dump collected information
  std::cout << counter.interior << " interior nodes" << std::endl;
  std::cout << counter.leaves << " leaf nodes" << std::endl;

  return EXIT_SUCCESS;
}
