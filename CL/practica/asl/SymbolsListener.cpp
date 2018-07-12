//////////////////////////////////////////////////////////////////////
//
//    SymbolsListener - Walk the parser tree to register symbols
//                      for the Asl programming language
//
//    Copyright (C) 2018  Universitat Politecnica de Catalunya
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU General Public License
//    as published by the Free Software Foundation; either version 3
//    of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Affero General Public License for more details.
//
//    You should have received a copy of the GNU Affero General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//
//    contact: José Miguel Rivero (rivero@cs.upc.edu)
//             Computer Science Department
//             Universitat Politecnica de Catalunya
//             despatx Omega.110 - Campus Nord UPC
//             08034 Barcelona.  SPAIN
//
//////////////////////////////////////////////////////////////////////

#include "SymbolsListener.h"

#include "antlr4-runtime.h"

#include "../common/TypesMgr.h"
#include "../common/SymTable.h"
#include "../common/TreeDecoration.h"
#include "../common/SemErrors.h"

#include <iostream>
#include <string>
#include <vector>

#include <cstddef>    // std::size_t

// uncomment the following line to enable debugging messages with DEBUG*
// #define DEBUG_BUILD
#include "../common/debug.h"

// using namespace std;


// Constructor
SymbolsListener::SymbolsListener(TypesMgr       & Types,
				 SymTable       & Symbols,
				 TreeDecoration & Decorations,
				 SemErrors      & Errors) :
  Types{Types},
  Symbols{Symbols},
  Decorations{Decorations},
  Errors{Errors} {
}

void SymbolsListener::enterProgram(AslParser::ProgramContext *ctx) {
  DEBUG_ENTER();
  SymTable::ScopeId sc = Symbols.pushNewScope("$global$");
  putScopeDecor(ctx, sc);
}
void SymbolsListener::exitProgram(AslParser::ProgramContext *ctx) {
  // Symbols.print();
  Symbols.popScope();
  DEBUG_EXIT();
}

void SymbolsListener::enterFunction(AslParser::FunctionContext *ctx) {
  DEBUG_ENTER();
  std::string funcName = ctx->ID()->getText();
  SymTable::ScopeId sc = Symbols.pushNewScope(funcName);
  putScopeDecor(ctx, sc);
}

void SymbolsListener::exitFunction(AslParser::FunctionContext *ctx) {
  // Symbols.print();
  Symbols.popScope();
  std::string ident = ctx->ID()->getText();
  if (Symbols.findInCurrentScope(ident)) {
    Errors.declaredIdent(ctx->ID());
  }
  else {
		std::vector<TypesMgr::TypeId> lParamsTy;

		for(auto param : ctx->param())
		{
			lParamsTy.push_back(getTypeDecor(param->type()));
		}

		TypesMgr::TypeId tRet;
		if(ctx->type())
		{
			tRet = getTypeDecor(ctx->type());
		}
		else
		{
			tRet = Types.createVoidTy();
		}

		TypesMgr::TypeId tFunc = Types.createFunctionTy(lParamsTy, tRet);
		Symbols.addFunction(ident, tFunc);
	}
	DEBUG_EXIT();
}


void SymbolsListener::enterParam(AslParser::ParamContext *ctx) {
	DEBUG_ENTER();
}

void SymbolsListener::exitParam(AslParser::ParamContext *ctx) {
	std::string ident = ctx->ID()->getText();
	if (Symbols.findInCurrentScope(ident)) {
		Errors.declaredIdent(ctx->ID());
	}
	else {
		TypesMgr::TypeId t1 = getTypeDecor(ctx->type());
		// DEBUG
		// Types.dump(t1);
		// std::cout << std::endl;
		Symbols.addParameter(ident, t1);
	}
	DEBUG_EXIT();
}

void SymbolsListener::enterDeclarations(AslParser::DeclarationsContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitDeclarations(AslParser::DeclarationsContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterVariable_decl(AslParser::Variable_declContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitVariable_decl(AslParser::Variable_declContext *ctx) {
	for(auto id : ctx->ID())
	{
		std::string ident = id->getText();
		if (Symbols.findInCurrentScope(ident)) {
			Errors.declaredIdent(id);
		}
		else {
			TypesMgr::TypeId t1 = getTypeDecor(ctx->type());
			Symbols.addLocalVar(ident, t1);
		}
	}
	DEBUG_EXIT();
}

void SymbolsListener::enterType(AslParser::TypeContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitType(AslParser::TypeContext *ctx) {
	TypesMgr::TypeId t;
	if (ctx->INT()) {
		t = Types.createIntegerTy();
	}
	else if (ctx->FLOAT()) {
		t = Types.createFloatTy();
	}
	else if (ctx->CHAR()) {
		t = Types.createCharacterTy();
	}
	else if (ctx->BOOL()) {
		t = Types.createBooleanTy();
	}

	else if (ctx->BOOL()) {
		t = Types.createBooleanTy();
	}
	else if (ctx->ARRAY()) {
		TypesMgr::TypeId t1;
		t1 = getTypeDecor(ctx->type());
		t = Types.createArrayTy( std::stoi(ctx->arrsize()->getText()), t1);
	}

	putTypeDecor(ctx, t);
	DEBUG_EXIT();
}

void SymbolsListener::enterStatements(AslParser::StatementsContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitStatements(AslParser::StatementsContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterAssignStmt(AslParser::AssignStmtContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitAssignStmt(AslParser::AssignStmtContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterIfStmt(AslParser::IfStmtContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitIfStmt(AslParser::IfStmtContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterProcCall(AslParser::ProcCallContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitProcCall(AslParser::ProcCallContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterReadStmt(AslParser::ReadStmtContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitReadStmt(AslParser::ReadStmtContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterWriteExpr(AslParser::WriteExprContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitWriteExpr(AslParser::WriteExprContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterWriteString(AslParser::WriteStringContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitWriteString(AslParser::WriteStringContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterLeft_expr(AslParser::Left_exprContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitLeft_expr(AslParser::Left_exprContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterArithmetic(AslParser::ArithmeticContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitArithmetic(AslParser::ArithmeticContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterParenthesis(AslParser::ParenthesisContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitParenthesis(AslParser::ParenthesisContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterRelational(AslParser::RelationalContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitRelational(AslParser::RelationalContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterValue(AslParser::ValueContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitValue(AslParser::ValueContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterExprIdent(AslParser::ExprIdentContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitExprIdent(AslParser::ExprIdentContext *ctx) {
	DEBUG_EXIT();
}

void SymbolsListener::enterIdent(AslParser::IdentContext *ctx) {
	DEBUG_ENTER();
}
void SymbolsListener::exitIdent(AslParser::IdentContext *ctx) {
	DEBUG_EXIT();
}

// void SymbolsListener::enterEveryRule(antlr4::ParserRuleContext *ctx) {
//   DEBUG_ENTER();
// }
// void SymbolsListener::exitEveryRule(antlr4::ParserRuleContext *ctx) {
//   DEBUG_EXIT();
// }
// void SymbolsListener::visitTerminal(antlr4::tree::TerminalNode *node) {
//   DEBUG("visitTerminal");
// }
// void SymbolsListener::visitErrorNode(antlr4::tree::ErrorNode *node) {
// }

// Getters for the necessary tree node atributes:
//   Scope and Type
SymTable::ScopeId SymbolsListener::getScopeDecor(antlr4::ParserRuleContext *ctx) {
	return Decorations.getScope(ctx);
}
TypesMgr::TypeId SymbolsListener::getTypeDecor(antlr4::ParserRuleContext *ctx) {
	return Decorations.getType(ctx);
}

// Setters for the necessary tree node attributes:
//   Scope and Type
void SymbolsListener::putScopeDecor(antlr4::ParserRuleContext *ctx, SymTable::ScopeId s) {
	Decorations.putScope(ctx, s);
}
void SymbolsListener::putTypeDecor(antlr4::ParserRuleContext *ctx, TypesMgr::TypeId t) {
	Decorations.putType(ctx, t);
}
