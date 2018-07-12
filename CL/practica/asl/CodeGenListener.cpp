//////////////////////////////////////////////////////////////////////
//
//    CodeGenListener - Walk the parser tree to do
//                             the generation of code
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

#include "CodeGenListener.h"

#include "antlr4-runtime.h"

#include "../common/TypesMgr.h"
#include "../common/SymTable.h"
#include "../common/TreeDecoration.h"
#include "../common/code.h"

#include <cstddef>    // std::size_t

// uncomment the following line to enable debugging messages with DEBUG*
// #define DEBUG_BUILD
#include "../common/debug.h"

// using namespace std;


// Constructor
CodeGenListener::CodeGenListener(TypesMgr       & Types,
		SymTable       & Symbols,
		TreeDecoration & Decorations,
		code           & Code) :
	Types{Types},
	Symbols{Symbols},
	Decorations{Decorations},
	Code{Code} {
	}

void CodeGenListener::enterProgram(AslParser::ProgramContext *ctx) {
	DEBUG_ENTER();
	SymTable::ScopeId sc = getScopeDecor(ctx);
	Symbols.pushThisScope(sc);
}
void CodeGenListener::exitProgram(AslParser::ProgramContext *ctx) {
	Symbols.popScope();
	DEBUG_EXIT();
}

void CodeGenListener::enterFunction(AslParser::FunctionContext *ctx) {
	DEBUG_ENTER();
	subroutine subr(ctx->ID()->getText());

	if (ctx->type()) {
		subr.add_param("_result");
	}
	if (ctx->param().size() > 0){
		for (auto param : ctx->param()) {
			subr.add_param(param->ID()->getText());
		}
	}

	Code.add_subroutine(subr);
	SymTable::ScopeId sc = getScopeDecor(ctx);
	Symbols.pushThisScope(sc);
	codeCounters.reset();
}
void CodeGenListener::exitFunction(AslParser::FunctionContext *ctx) {
	subroutine & subrRef = Code.get_last_subroutine();
	instructionList code = getCodeDecor(ctx->statements());
	code = code || instruction::RETURN();
	subrRef.set_instructions(code);
	Symbols.popScope();
	DEBUG_EXIT();
}

void CodeGenListener::enterDeclarations(AslParser::DeclarationsContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitDeclarations(AslParser::DeclarationsContext *ctx) {
	DEBUG_EXIT();
}

void CodeGenListener::enterVariable_decl(AslParser::Variable_declContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitVariable_decl(AslParser::Variable_declContext *ctx) {
	subroutine       & subrRef = Code.get_last_subroutine();
	TypesMgr::TypeId        t1 = getTypeDecor(ctx->type());
	std::size_t           size = Types.getSizeOfType(t1);
	for(auto id : ctx->ID())
	{
		subrRef.add_var(id->getText(), size);
		// DEBUG
		// std::cout << "name: " << id->getText() << " typeName: " << 
		// 	ctx->type()->getText() << " typeId: " << t1 << " size: " <<
		// 	size << std::endl;
	} 
	DEBUG_EXIT();
}

void CodeGenListener::enterType(AslParser::TypeContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitType(AslParser::TypeContext *ctx) {
	DEBUG_EXIT();
}

void CodeGenListener::enterStatements(AslParser::StatementsContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitStatements(AslParser::StatementsContext *ctx) {
	instructionList code;
	for (auto stCtx : ctx->statement()) {
		code = code || getCodeDecor(stCtx);
	}
	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterAssignStmt(AslParser::AssignStmtContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitAssignStmt(AslParser::AssignStmtContext *ctx) {
	auto addr1 = getAddrDecor(ctx->left_expr());
	auto offs1 = getOffsetDecor(ctx->left_expr());
	auto code1 = getCodeDecor(ctx->left_expr());
	// TypesMgr::TypeId tid1 = getTypeDecor(ctx->left_expr());
	auto addr2 = getAddrDecor(ctx->expr());
	// std::string     offs2 = getOffsetDecor(ctx->expr());
	auto code2 = getCodeDecor(ctx->expr());
	// TypesMgr::TypeId tid2 = getTypeDecor(ctx->expr());
	auto code = code1 || code2;

	if(offs1 != "")
	{
		auto identName = ctx->left_expr()->ident()->ID()->getText();

		if (Symbols.isParameterClass(identName)) {
			auto tmp = "%" + codeCounters.newTEMP();
			code = code || instruction::LOAD(tmp, addr1);
			addr1 = tmp;
		}

		code = code || instruction::XLOAD(addr1, offs1, addr2);
		putCodeDecor(ctx, code);
		DEBUG_EXIT();
		return;
	}

	code = code || instruction::LOAD(addr1, addr2);
	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterIfStmt(AslParser::IfStmtContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitIfStmt(AslParser::IfStmtContext *ctx) {
	instructionList code;
	auto addr1 = getAddrDecor(ctx->expr());
	auto code1 = getCodeDecor(ctx->expr());
	auto code2 = getCodeDecor(ctx->statements(0));

	auto label = codeCounters.newLabelIF();
	auto labelEndIf = "endif"+label;

	if(ctx->ELSE())
	{
		auto code3 = getCodeDecor(ctx->statements(1));
		auto labelElse = "else"+label;

		code = 
			code1 ||
			instruction::FJUMP(addr1, labelElse) ||
			code2 ||
			instruction::LABEL(labelElse) ||
			code3 ||
			instruction::LABEL(labelEndIf);

		putCodeDecor(ctx, code);
		DEBUG_EXIT();
		return;
	}

	code = code1 || instruction::FJUMP(addr1, labelEndIf) ||
		code2 || instruction::LABEL(labelEndIf);

	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterProcCall(AslParser::ProcCallContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitProcCall(AslParser::ProcCallContext *ctx) {
	auto code = getCodeDecor(ctx->procedure());
	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterExprProcCall(AslParser::ExprProcCallContext *ctx){
	DEBUG_ENTER();
}

void CodeGenListener::exitExprProcCall(AslParser::ExprProcCallContext *ctx){
	auto addr = getAddrDecor(ctx->procedure());
	auto code = getCodeDecor(ctx->procedure());
	putAddrDecor(ctx, addr);
	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterProcedure(AslParser::ProcedureContext *ctx) {
	DEBUG_ENTER();
}

void CodeGenListener::exitProcedure(AslParser::ProcedureContext *ctx) {
	instructionList code;
	auto funcName = ctx->ident()->ID()->getText();
	auto funcType = Symbols.getType(funcName);

	if (not Types.isVoidFunction(funcType))
	{
		code = code || instruction::PUSH();
	}

	auto paramNum = ctx->expr().size();
	auto t1 = getTypeDecor(ctx->ident());

	// Push the parameters
	for (int i = 0; i < paramNum; ++i) {
		auto expr = ctx->expr()[i];
		auto exprAddr = getAddrDecor(expr);
		auto exprCode = getCodeDecor(expr);

		auto paramTy = Types.getParameterType(t1, i);
		auto exprTy = getTypeDecor(expr);

		if(Types.isFloatTy(paramTy) && Types.isIntegerTy(exprTy)){
			auto f_addr = "%" + codeCounters.newTEMP();
			exprCode = exprCode || instruction::FLOAT(f_addr, exprAddr);
			exprAddr = f_addr;
		}
		else if (Types.isArrayTy(paramTy)) {
			auto a_addr = "%" + codeCounters.newTEMP();
			exprCode = exprCode || instruction::ALOAD(a_addr, exprAddr);
			exprAddr = a_addr;
		}

		code = code || exprCode || instruction::PUSH(exprAddr) ;
	}

	// Call the function
	code = code || instruction::CALL(funcName);

	// Pop parameters
	for (int i = 0; i < paramNum; ++i) {
		code = code || instruction::POP();
	}

	// Get the return value
	if (not Types.isVoidFunction(funcType)){
		auto addr = "%" + codeCounters.newTEMP();
		code = code || instruction::POP(addr);
		putAddrDecor(ctx, addr);
	}

	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterReadStmt(AslParser::ReadStmtContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitReadStmt(AslParser::ReadStmtContext *ctx) {
	instructionList  code;
	auto addr1 = getAddrDecor(ctx->left_expr());
	auto offs1 = getOffsetDecor(ctx->left_expr());
	auto code1 = getCodeDecor(ctx->left_expr());
	auto tid1 = getTypeDecor(ctx->left_expr());

	bool isArray = ctx->left_expr()->ident()->expr();
	std::string arrayAddr;

	if(isArray){
		arrayAddr = addr1;
		addr1 = '%' + codeCounters.newTEMP();
	}

	if (Types.isIntegerTy(tid1)) 
	{
		code = code1 || instruction::READI(addr1);
	}
	else if (Types.isFloatTy(tid1)) 
	{
		code = code1 || instruction::READF(addr1);
	}
	else if (Types.isCharacterTy(tid1)) 
	{
		code = code1 || instruction::READC(addr1);
	}
	else //if (Types.isBooleanTy(tid1)) 
	{
		code = code1 || instruction::READI(addr1);
	}

	if(isArray)
	{
		code = code ||  instruction::XLOAD(arrayAddr, offs1, addr1);
	}

	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterWriteExpr(AslParser::WriteExprContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitWriteExpr(AslParser::WriteExprContext *ctx) {
	instructionList code;
	std::string     addr1 = getAddrDecor(ctx->expr());
	// std::string     offs1 = getOffsetDecor(ctx->expr());
	instructionList code1 = getCodeDecor(ctx->expr());
	TypesMgr::TypeId tid1 = getTypeDecor(ctx->expr());

	if(Types.isFloatTy(tid1))
	{
		code = code1 || instruction::WRITEF(addr1);
	}
	else
	{
		code = code1 || instruction::WRITEI(addr1);
	}

	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterWriteString(AslParser::WriteStringContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitWriteString(AslParser::WriteStringContext *ctx) {
	instructionList code;
	std::string s = ctx->STRING()->getText();
	std::string temp = "%"+codeCounters.newTEMP();
	int i = 1;
	while (i < int(s.size())-1) {
		if (s[i] != '\\') {
			code = code ||
				instruction::CHLOAD(temp, s.substr(i,1)) ||
				instruction::WRITEC(temp);
			i += 1;
		}
		else {
			assert(i < int(s.size())-2);
			if (s[i+1] == 'n') {
				code = code || instruction::WRITELN();
				i += 2;
			}
			else if (s[i+1] == 't' or s[i+1] == '"' or s[i+1] == '\\') {
				code = code ||
					instruction::CHLOAD(temp, s.substr(i,2)) ||
					instruction::WRITEC(temp);
				i += 2;
			}
			else {
				code = code ||
					instruction::CHLOAD(temp, s.substr(i,1)) ||
					instruction::WRITEC(temp);
				i += 1;
			}
		}
	}
	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterLeft_expr(AslParser::Left_exprContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitLeft_expr(AslParser::Left_exprContext *ctx) {
	putAddrDecor(ctx, getAddrDecor(ctx->ident()));
	putOffsetDecor(ctx, getOffsetDecor(ctx->ident()));
	putCodeDecor(ctx, getCodeDecor(ctx->ident()));
	DEBUG_EXIT();
}

void CodeGenListener::enterArithmetic(AslParser::ArithmeticContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitArithmetic(AslParser::ArithmeticContext *ctx) {

	std::string     addr1 = getAddrDecor(ctx->expr(0));
	instructionList code1 = getCodeDecor(ctx->expr(0));


	instructionList code  = code1;

	TypesMgr::TypeId t1 = getTypeDecor(ctx->expr(0));

	if(!ctx->expr(1) && ctx->SUB())
	{
		auto unary = "%"+codeCounters.newTEMP();

		if(Types.isFloatTy(t1))
		{
			code = code || instruction::FNEG(unary, addr1);
		}
		else
		{
			code = code || instruction::NEG(unary, addr1);
		}

		putAddrDecor(ctx, unary);
		putOffsetDecor(ctx, "");
		putCodeDecor(ctx, code);
		DEBUG_EXIT();
		return;
	}

	std::string     addr2 = getAddrDecor(ctx->expr(1));
	instructionList code2 = getCodeDecor(ctx->expr(1));

	code  = code || code2;

	TypesMgr::TypeId t2 = getTypeDecor(ctx->expr(1));
	TypesMgr::TypeId t  = getTypeDecor(ctx);

	// std::cout << "Arithmetic context type:" << std::endl;
	// Types.dump(t1);
	// std::cout << std::endl;
	// Types.dump(t2);
	// std::cout << std::endl;
	// std::cout << std::endl;
	// Types.dump(t);
	// std::cout << std::endl;


	if(Types.isFloatTy(t1) && !Types.isFloatTy(t2))
	{
		std::string ftemp = "%"+codeCounters.newTEMP();
		code = code || instruction::FLOAT(ftemp, addr2);
		addr2 = ftemp;
	}

	if(Types.isFloatTy(t2) && !Types.isFloatTy(t1))
	{
		std::string ftemp = "%"+codeCounters.newTEMP();
		code = code || instruction::FLOAT(ftemp, addr1);
		addr1 = ftemp;
	}

	std::string temp = "%"+codeCounters.newTEMP();

	if (ctx->MUL())
	{
		if(Types.isFloatTy(t))
			code = code || instruction::FMUL(temp, addr1, addr2);
		else
			code = code || instruction::MUL(temp, addr1, addr2);
	}
	else if(ctx->DIV()) 
	{	

		if(Types.isFloatTy(t))
			code = code || instruction::FDIV(temp, addr1, addr2);
		else
			code = code || instruction::DIV(temp, addr1, addr2);
	}
	else if(ctx->MOD()) 
	{
		std::string temp1 = "%"+codeCounters.newTEMP();
		std::string temp2 = "%"+codeCounters.newTEMP();
		code = code || instruction::DIV(temp1, addr1, addr2);
		code = code || instruction::MUL(temp2, temp1, addr2);
		code = code || instruction::SUB(temp, addr1, temp2);
	}
	else if(ctx->ADD()) 
	{
		if(Types.isFloatTy(t))
			code = code || instruction::FADD(temp, addr1, addr2);
		else
			code = code || instruction::ADD(temp, addr1, addr2);
	}
	else if(ctx->SUB()) 
	{
		if(Types.isFloatTy(t))
			code = code || instruction::FSUB(temp, addr1, addr2);
		else
			code = code || instruction::SUB(temp, addr1, addr2);
	}

	putAddrDecor(ctx, temp);
	putOffsetDecor(ctx, "");
	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterParenthesis(AslParser::ParenthesisContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitParenthesis(AslParser::ParenthesisContext *ctx) {
	putAddrDecor(ctx, getAddrDecor(ctx->expr()));
	putOffsetDecor(ctx, getOffsetDecor(ctx->expr()));
	putCodeDecor(ctx, getCodeDecor(ctx->expr()));
	DEBUG_EXIT();
}

void CodeGenListener::enterRelational(AslParser::RelationalContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitRelational(AslParser::RelationalContext *ctx) {
	std::string     addr1 = getAddrDecor(ctx->expr(0));
	instructionList code1 = getCodeDecor(ctx->expr(0));
	std::string     addr2 = getAddrDecor(ctx->expr(1));
	instructionList code2 = getCodeDecor(ctx->expr(1));
	instructionList code  = code1 || code2;

	// TypesMgr::TypeId t1 = getTypeDecor(ctx->expr(0));
	// TypesMgr::TypeId t2 = getTypeDecor(ctx->expr(1));
	// TypesMgr::TypeId t  = getTypeDecor(ctx);

	std::string temp = "%"+codeCounters.newTEMP();
	if (ctx->EQ())
	{
		code = code || instruction::EQ(temp, addr1, addr2);
	}
	else if (ctx->NE())
	{
		code = code || instruction::EQ(temp, addr1, addr2);
		code = code || instruction::NOT(temp, temp);
	}
	else if (ctx->GE())
	{
		code = code || instruction::LE(temp, addr2, addr1);
	}
	else if (ctx->LE())
	{
		code = code || instruction::LE(temp, addr1, addr2);
	}
	else if (ctx->GT())
	{
		code = code || instruction::LT(temp, addr2, addr1);
	}
	else //if (ctx->LT())
	{
		code = code || instruction::LT(temp, addr1, addr2);
	}

	putAddrDecor(ctx, temp);
	putOffsetDecor(ctx, "");
	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterBoolean(AslParser::BooleanContext *ctx) {
	DEBUG_ENTER();
}

void CodeGenListener::exitBoolean(AslParser::BooleanContext *ctx) {

	if(ctx->NOT())
	{
		std::string     addr = getAddrDecor(ctx->expr(0));
		instructionList code = getCodeDecor(ctx->expr(0));
		std::string temp = "%"+codeCounters.newTEMP();
		code = code || instruction::NOT(temp, addr);
		putAddrDecor(ctx, temp);
		putOffsetDecor(ctx, "");
		putCodeDecor(ctx, code);
		DEBUG_EXIT();
		return;
	}

	std::string     addr1 = getAddrDecor(ctx->expr(0));
	instructionList code1 = getCodeDecor(ctx->expr(0));
	std::string     addr2 = getAddrDecor(ctx->expr(1));
	instructionList code2 = getCodeDecor(ctx->expr(1));
	instructionList code  = code1 || code2;
	// TypesMgr::TypeId t1 = getTypeDecor(ctx->expr(0));
	// TypesMgr::TypeId t2 = getTypeDecor(ctx->expr(1));
	// TypesMgr::TypeId t  = getTypeDecor(ctx);
	std::string temp = "%"+codeCounters.newTEMP();
	if (ctx->AND())
	{
		code = code || instruction::AND(temp, addr1, addr2);
	}
	else //if (ctx->OR())
	{
		code = code || instruction::OR(temp, addr2, addr1);
	}

	putAddrDecor(ctx, temp);
	putOffsetDecor(ctx, "");
	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterValue(AslParser::ValueContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitValue(AslParser::ValueContext *ctx) {
	instructionList code;
	std::string temp = "%"+codeCounters.newTEMP();
	std::string text = ctx->getText();

	if (ctx->INTVAL())
	{
		code = instruction::ILOAD(temp, text);
	}
	if (ctx->FLOATVAL())
	{
		code = instruction::FLOAD(temp, text);
	}
	if (ctx->CHARVAL())
	{
		code = instruction::CHLOAD(temp, text);
	}
	if(ctx->BOOLVAL())
	{
		if(text == "true")
		{
			text = "1";
		}
		else //(text == "false")
		{
			text = "0";
		}
		code = instruction::ILOAD(temp, text);
	}

	putAddrDecor(ctx, temp);
	putOffsetDecor(ctx, "");
	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterExprIdent(AslParser::ExprIdentContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitExprIdent(AslParser::ExprIdentContext *ctx) {
	auto addr = getAddrDecor(ctx->ident());
	auto offset = getOffsetDecor(ctx->ident());
	auto code = getCodeDecor(ctx->ident());

	if(offset != "")
	{
		auto identName = ctx->ident()->ID()->getText();

		if (Symbols.isParameterClass(identName)) {
			auto tmp = "%" + codeCounters.newTEMP();
			code = code || instruction::LOAD(tmp, addr);
			addr = tmp;
		}

		auto tmp = "%" + codeCounters.newTEMP();
		code = code || instruction::LOADX(tmp, addr, offset);
		addr = tmp;
	}

	putAddrDecor(ctx, addr);
	putOffsetDecor(ctx, offset);
	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterIdent(AslParser::IdentContext *ctx) {
	DEBUG_ENTER();
}
void CodeGenListener::exitIdent(AslParser::IdentContext *ctx) {
	if (ctx->expr()) {
		auto t1 = getTypeDecor(ctx);
		auto size = Types.getSizeOfType(t1);
		auto code = getCodeDecor(ctx->expr());
		auto addr = getAddrDecor(ctx->expr());

		auto temp = "%" + codeCounters.newTEMP();
		auto offset = "%" + codeCounters.newTEMP();

		code = code ||
			instruction::ILOAD(temp, std::to_string(size)) ||
			instruction::MUL(offset, temp, addr);

		putAddrDecor(ctx, ctx->ID()->getText());
		putOffsetDecor(ctx, offset);
		putCodeDecor(ctx, code);
		DEBUG_EXIT();
		return;
	}

	putAddrDecor(ctx, ctx->ID()->getText());
	putOffsetDecor(ctx, "");
	putCodeDecor(ctx, instructionList());
	DEBUG_EXIT();
}

void CodeGenListener::enterWhileStmt(AslParser::WhileStmtContext *ctx) {
	DEBUG_ENTER();
}

void CodeGenListener::exitWhileStmt(AslParser::WhileStmtContext *ctx) {
	std::string      label = codeCounters.newLabelWHILE();
	std::string      labelDo = "do" + label;
	std::string      labelWhile = "while" + label;
	instructionList code = 
		instruction::UJUMP(labelWhile) ||
		instruction::LABEL(labelDo);

	code = code || getCodeDecor(ctx->statements());

	code = code ||
		instruction::LABEL(labelWhile) ||
		getCodeDecor(ctx->expr());

	std::string addrExpr = getAddrDecor(ctx->expr());
	std::string tmp = "%" + codeCounters.newTEMP();

	code = code ||
		instruction::NOT(tmp, addrExpr) ||
		instruction::FJUMP(tmp, labelDo);

	putCodeDecor(ctx, code);
	DEBUG_EXIT();
}

void CodeGenListener::enterReturnStmt(AslParser::ReturnStmtContext *ctx) {
	DEBUG_ENTER();
}

void CodeGenListener::exitReturnStmt(AslParser::ReturnStmtContext *ctx) {
	if (ctx->ret_expr()) {
		instructionList  code;
		instructionList retExpr = getCodeDecor(ctx->ret_expr()->expr());
		std::string retAddr = getAddrDecor(ctx->ret_expr()->expr());
		code = retExpr || instruction::LOAD("_result", retAddr);
		putCodeDecor(ctx, code);
	}
	DEBUG_EXIT();
}

// void CodeGenListener::enterEveryRule(antlr4::ParserRuleContext *ctx) {
//   DEBUG_ENTER();
// }
// void CodeGenListener::exitEveryRule(antlr4::ParserRuleContext *ctx) {
//   DEBUG_EXIT();
// }
// void CodeGenListener::visitTerminal(antlr4::tree::TerminalNode *node) {
//   DEBUG(">>> visit " << node->getSymbol()->getLine() << ":" << node->getSymbol()->getCharPositionInLine() << " CodeGen TerminalNode");
// }
// void CodeGenListener::visitErrorNode(antlr4::tree::ErrorNode *node) {
// }


// Getters for the necessary tree node atributes:
//   Scope, Type, Addr, Offset and Code
SymTable::ScopeId CodeGenListener::getScopeDecor(antlr4::ParserRuleContext *ctx) {
	return Decorations.getScope(ctx);
}
TypesMgr::TypeId CodeGenListener::getTypeDecor(antlr4::ParserRuleContext *ctx) {
	return Decorations.getType(ctx);
}
std::string CodeGenListener::getAddrDecor(antlr4::ParserRuleContext *ctx) {
	return Decorations.getAddr(ctx);
}
std::string  CodeGenListener::getOffsetDecor(antlr4::ParserRuleContext *ctx) {
	return Decorations.getOffset(ctx);
}
instructionList CodeGenListener::getCodeDecor(antlr4::ParserRuleContext *ctx) {
	return Decorations.getCode(ctx);
}

// Setters for the necessary tree node attributes:
//   Addr, Offset and Code
void CodeGenListener::putAddrDecor(antlr4::ParserRuleContext *ctx, const std::string & a) {
	Decorations.putAddr(ctx, a);
}
void CodeGenListener::putOffsetDecor(antlr4::ParserRuleContext *ctx, const std::string & o) {
	Decorations.putOffset(ctx, o);
}
void CodeGenListener::putCodeDecor(antlr4::ParserRuleContext *ctx, const instructionList & c) {
	Decorations.putCode(ctx, c);
}
