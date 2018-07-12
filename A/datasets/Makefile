CC=g++
CFLAGS=
LDFLAGS= -Wfatal-errors

EXECUTABLES = $(FLT_EXE) $(PRM_EXE)
SOURCES			= $(FLT_EXE_SRC) $(PRM_EXE_SRC)

FLT_SRC					= ../Victor/filter.cc

FLT_EXE					= filter.out
FLT_EXE_SRC			= filter_main.cc ../Victor/utils.cc $(FLT_SRC)

PRM_SRC					=	../Victor/permgen.cc

PRM_EXE 				= permgen.out
PRM_EXE_SRC			=	permgen_main.cc ../Victor/utils.cc $(PRM_SRC)

all: $(EXECUTABLES)

# filter test
$(FLT_EXE): $(FLT_EXE_SRC)
	$(CC) $(LDFLAGS) $(FLT_EXE_SRC) -o $@

# permgen test
$(PRM_EXE): $(PRM_EXE_SRC)
	$(CC) $(LDFLAGS) $(PRM_EXE_SRC) -o $@

clean:
	rm *.out
