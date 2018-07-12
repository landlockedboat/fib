INCL=-I/usr/include/pccts/
FLAGS=-Wno-write-strings
SOURCES=karel.c scan.c err.c

EXECUTABLE=karel

all: $(EXECUTABLE)

$(EXECUTABLE): parser.dlg karel.g $(SOURCES)
	antlr -gt karel.g
	dlg -ci parser.dlg scan.c
	g++ -o $@ $(SOURCES) $(INCL) $(FLAGS)

clean:
	rm $(EXECUTABLE)
	rm *.o
setenv:
	echo "setenv PATH /opt/pccts/bin/:$$PATH"
