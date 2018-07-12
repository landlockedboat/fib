#ifndef tokens_h
#define tokens_h
/* tokens.h -- List of labelled tokens and stuff
 *
 * Generated from: karel.g
 *
 * Terence Parr, Will Cohen, and Hank Dietz: 1989-2001
 * Purdue University Electrical Engineering
 * ANTLR Version 1.33MR33
 */
#define zzEOF_TOKEN 1
#define WORLD 2
#define ROBOT 3
#define WALLS 4
#define BEEPERS 5
#define ISCLEAR 6
#define ANYBEEPERS 7
#define TURNLEFT 8
#define MOVE 9
#define PUTBEEPER 10
#define PICKBEEPER 11
#define FOUNDBEEPER 12
#define ITERATE 13
#define DEFINE 14
#define BEGIN 15
#define END 16
#define TURNOFF 17
#define IF 18
#define AND 19
#define OR 20
#define NOT 21
#define RIGHT 22
#define LEFT 23
#define UP 24
#define DOWN 25
#define ID 26
#define NUM 27
#define LCLA 28
#define RCLA 29
#define LBRA 30
#define RBRA 31
#define COMMA 32
#define SEMI 33
#define SPACE 34

#ifdef __USE_PROTOS
void karel(AST**_root);
#else
extern void karel();
#endif

#ifdef __USE_PROTOS
void world(AST**_root);
#else
extern void world();
#endif

#ifdef __USE_PROTOS
void robot(AST**_root);
#else
extern void robot();
#endif

#ifdef __USE_PROTOS
void inits(AST**_root);
#else
extern void inits();
#endif

#ifdef __USE_PROTOS
void walls(AST**_root);
#else
extern void walls();
#endif

#ifdef __USE_PROTOS
void beepers(AST**_root);
#else
extern void beepers();
#endif

#ifdef __USE_PROTOS
void def(AST**_root);
#else
extern void def();
#endif

#ifdef __USE_PROTOS
void instrs(AST**_root);
#else
extern void instrs();
#endif

#ifdef __USE_PROTOS
void instruction(AST**_root);
#else
extern void instruction();
#endif

#ifdef __USE_PROTOS
void specinstr(AST**_root);
#else
extern void specinstr();
#endif

#ifdef __USE_PROTOS
void condinst(AST**_root);
#else
extern void condinst();
#endif

#ifdef __USE_PROTOS
void condition(AST**_root);
#else
extern void condition();
#endif

#ifdef __USE_PROTOS
void lelement(AST**_root);
#else
extern void lelement();
#endif

#ifdef __USE_PROTOS
void loop(AST**_root);
#else
extern void loop();
#endif

#ifdef __USE_PROTOS
void direction(AST**_root);
#else
extern void direction();
#endif

#endif
extern SetWordType setwd1[];
extern SetWordType zzerr1[];
extern SetWordType zzerr2[];
extern SetWordType zzerr3[];
extern SetWordType setwd2[];
extern SetWordType zzerr4[];
extern SetWordType zzerr5[];
extern SetWordType zzerr6[];
extern SetWordType setwd3[];
