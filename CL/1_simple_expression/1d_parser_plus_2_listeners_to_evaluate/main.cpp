
#include "antlr4-runtime.h"
#include "ExprLexer.h"
#include "ExprParser.h"
#include "ExprBaseListener.h"
#include "tree/ParseTreeWalker.h"
#include "tree/ParseTreeProperty.h"

#include <iostream>
#include <fstream>
#include <stack>
#include <cstdio>    // fopen

// using namespace std;
// using namespace antlr4;


//////////////////////////////////////////////////////////////////////
// Sample "calculator" (special case of collector)
class Evaluator : public ExprBaseListener {
public:
  
  std::stack<int> stack;

  void exitE(ExprParser::EContext *ctx) {
    if (ctx->children.size() == 3) { // operations have 3 children
      int right = stack.top();
      stack.pop();
      int left = stack.top();
      stack.pop();
      if (ctx->op->getType() == ExprParser::MULT) {
        stack.push(left*right);
      }
      else {
        stack.push(left+right); // must be add
      }
    }    
  }

  void visitTerminal(antlr4::tree::TerminalNode *node) {
    antlr4::Token *symbol = node->getSymbol();
    if (symbol->getType() == ExprParser::INT) {
      stack.push(std::stoi(symbol->getText()));
    }
  }

};
// Sample "calculator" (special case of collector)
//////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////
// Sample "calculator" using tree properties not stack
class EvaluatorWithProps : public ExprBaseListener {
public:

  antlr4::tree::ParseTreeProperty<int> values;

  void exitS(ExprParser::SContext *ctx) {
    values.put(ctx, values.get(ctx->e()));
  }

  void exitE(ExprParser::EContext *ctx) {
    if (ctx->children.size() == 3) { // operations have 3 children
      int left = values.get(ctx->e(0));
      int right = values.get(ctx->e(1));
      if (ctx->op->getType() == ExprParser::MULT) {
        values.put(ctx, left*right);
      }
      else {
        values.put(ctx, left+right);
      }
    }
    else {
      values.put(ctx, values.get(ctx->INT())); // an INT
    }
  }

  void visitTerminal(antlr4::tree::TerminalNode *node) {
    antlr4::Token *symbol = node->getSymbol();
    if (symbol->getType() == ExprParser::INT) {
      values.put(node, std::stoi(symbol->getText()));
    }
  }

};
// Sample "calculator" using tree properties not stack
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
  
  // Create a first listener (with stack) that will evaluate the expression
  Evaluator eval;

  // Traverse the tree using this Evaluator  
  walker.walk(&eval, tree);

  // Dump the result (on top of the stack)
  std::cout << "result = " << eval.stack.top() << std::endl;
  
  // Create a second listener (with Properties) that will evaluate the expression
  EvaluatorWithProps eval2;

  // Traverse the tree using this EvaluatorWithProps
  walker.walk(&eval2, tree);

  // Dump the result (accessing the root node property)
  std::cout << "result with tree props = " << eval2.values.get(tree) << std::endl;

  return EXIT_SUCCESS;
}
