
// Generated from Expr.g4 by ANTLR 4.7.1

#pragma once


#include "antlr4-runtime.h"
#include "ExprListener.h"


/**
 * This class provides an empty implementation of ExprListener,
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
class  ExprBaseListener : public ExprListener {
public:

  virtual void enterS(ExprParser::SContext * /*ctx*/) override { }
  virtual void exitS(ExprParser::SContext * /*ctx*/) override { }

  virtual void enterProd(ExprParser::ProdContext * /*ctx*/) override { }
  virtual void exitProd(ExprParser::ProdContext * /*ctx*/) override { }

  virtual void enterValue(ExprParser::ValueContext * /*ctx*/) override { }
  virtual void exitValue(ExprParser::ValueContext * /*ctx*/) override { }

  virtual void enterPlus(ExprParser::PlusContext * /*ctx*/) override { }
  virtual void exitPlus(ExprParser::PlusContext * /*ctx*/) override { }


  virtual void enterEveryRule(antlr4::ParserRuleContext * /*ctx*/) override { }
  virtual void exitEveryRule(antlr4::ParserRuleContext * /*ctx*/) override { }
  virtual void visitTerminal(antlr4::tree::TerminalNode * /*node*/) override { }
  virtual void visitErrorNode(antlr4::tree::ErrorNode * /*node*/) override { }

};

