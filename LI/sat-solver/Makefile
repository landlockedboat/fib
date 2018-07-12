CC=SAT-alumnes.cpp
EXE=SAT.out
TMP=res.tmp

all: $(EXE)

$(EXE): $(CC)
	g++ $(CC) -o $(EXE)

test:
	bash -c "./tests/test.sh"

clean:
	rm *.out
