
// Generated from Expr.g4 by ANTLR 4.7.1

#pragma once


#include "antlr4-runtime.h"
#include "ExprParser.h"


/**
 * This interface defines an abstract listener for a parse tree produced by ExprParser.
 */
class  ExprListener : public antlr4::tree::ParseTreeListener {
public:

  virtual void enterS(ExprParser::SContext *ctx) = 0;
  virtual void exitS(ExprParser::SContext *ctx) = 0;

  virtual void enterProd(ExprParser::ProdContext *ctx) = 0;
  virtual void exitProd(ExprParser::ProdContext *ctx) = 0;

  virtual void enterValue(ExprParser::ValueContext *ctx) = 0;
  virtual void exitValue(ExprParser::ValueContext *ctx) = 0;

  virtual void enterPlus(ExprParser::PlusContext *ctx) = 0;
  virtual void exitPlus(ExprParser::PlusContext *ctx) = 0;


};

