all: Jaccard_test MinHash_test MinHashMat_CharMat_test

Jaccard_test: Jaccard_test.cc Jaccard.cc
	g++ Jaccard_test.cc Jaccard.cc -o Jaccard_test.out

MinHash_test: MinHash_test.cc MinHash.cc
	g++ MinHash_test.cc MinHash.cc -o MinHash_test.out
  
MinHashMat_CharMat_test: MinHashMat_CharMat_test.cc CharMat.cc MinHashMat.cc
	g++ MinHashMat_CharMat_test.cc CharMat.cc MinHashMat.cc -o MinHashMat_CharMat_test.out

clean:
	rm *.o *.out
