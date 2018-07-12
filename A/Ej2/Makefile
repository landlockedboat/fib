CC=g++
CFLAGS=
LDFLAGS= -Wfatal-errors

EXECUTABLES = $(MIN_EXE)
SOURCES			= $(MIN_EXE_SRC)

MIN_SRC					= ../Alvar/MinHashMat.cc ../Alvar/CharMat.cc

MIN_EXE					= minhash.out
MIN_EXE_SRC			= minhash_main.cc jaccard_minhash.cc utils.cc $(MIN_SRC) # ShingleSetMaker.cc

all: $(EXECUTABLES)

# filter test
$(MIN_EXE): $(MIN_EXE_SRC)
	$(CC) $(LDFLAGS) $(MIN_EXE_SRC) -o $@

clean:
	rm *.out
