CC=g++
CFLAGS=
LDFLAGS= 

BIN_PATH = ../bin/
MAINS_PATH = mains/
IMPORTS_PATH = imports/
EXECUTABLES = $(MIN_EXE) $(FLT_EXE) $(PRM_EXE) $(JACC_EXE) $(LSH_EXE) $(JACC_PAR_EXE)
SOURCES			= $(MIN_EXE_SRC) $(FLT_EXE_SRC) $(PRM_EXE_SRC) $(JACC_EXE_SRC) $(LSH_EXE_SRC) $(JACC_PAR_EXE_SRC)

JACC_PAR_SRC		= $(IMPORTS_PATH)Jaccard.cc

JACC_PAR_EXE 				= $(BIN_PATH)jaccard_pairs.out
JACC_PAR_EXE_SRC		=	$(MAINS_PATH)jaccard_pairs.cc $(JACC_PAR_SRC)

LSH_SRC				= $(IMPORTS_PATH)MinHash.cc

LSH_EXE 				= $(BIN_PATH)lsh.out
LSH_EXE_SRC		=	$(MAINS_PATH)lsh.cc $(LSH_SRC)

JACC_SRC				= $(IMPORTS_PATH)Jaccard.cc

JACC_EXE 				= $(BIN_PATH)jaccard.out
JACC_EXE_SRC		=	$(MAINS_PATH)jaccard.cc $(JACC_SRC)

MIN_SRC					= $(IMPORTS_PATH)MinHashMat.cc $(IMPORTS_PATH)CharMat.cc

MIN_EXE					= $(BIN_PATH)minhash.out
MIN_EXE_SRC			= $(MAINS_PATH)minhash_main.cc $(IMPORTS_PATH)jaccard_minhash.cc $(IMPORTS_PATH)utils.cc $(MIN_SRC)

FLT_SRC					= $(IMPORTS_PATH)filter.cc

FLT_EXE					= $(BIN_PATH)filter.out
FLT_EXE_SRC			= $(MAINS_PATH)filter_main.cc $(IMPORTS_PATH)utils.cc $(FLT_SRC)

PRM_SRC					=	$(IMPORTS_PATH)permgen.cc

PRM_EXE 				= $(BIN_PATH)permgen.out
PRM_EXE_SRC			=	$(MAINS_PATH)permgen_main.cc $(IMPORTS_PATH)utils.cc $(PRM_SRC)

all: $(EXECUTABLES)



# jaccard.out
$(JACC_EXE): $(JACC_EXE_SRC)
	$(CC) $(LDFLAGS) $(JACC_EXE_SRC) -o $@

# minhash.out
$(MIN_EXE): $(MIN_EXE_SRC)
	$(CC) $(LDFLAGS) $(MIN_EXE_SRC) -o $@

#lsh.out
$(LSH_EXE): $(LSH_EXE_SRC)
	$(CC) $(LDFLAGS) $(LSH_EXE_SRC) -o $@

#jaccard_pairs.out
$(JACC_PAR_EXE): $(JACC_PAR_EXE_SRC)
	$(CC) $(LDFLAGS) $(JACC_PAR_EXE_SRC) -o $@

# filter.out
$(FLT_EXE): $(FLT_EXE_SRC)
	$(CC) $(LDFLAGS) $(FLT_EXE_SRC) -o $@

# permgen.out
$(PRM_EXE): $(PRM_EXE_SRC)
	$(CC) $(LDFLAGS) $(PRM_EXE_SRC) -o $@



clean:
	rm $(BIN_PATH)*.out
