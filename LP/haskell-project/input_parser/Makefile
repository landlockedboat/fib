INCL=-I/usr/include/pccts/
FLAGS=-Wno-write-strings
SOURCES=reader.c scan.c err.c

EXECUTABLE=reader

all: $(EXECUTABLE)

$(EXECUTABLE): parser.dlg reader.g $(SOURCES)
	antlr -gt reader.g
	dlg -ci parser.dlg scan.c
	g++ -o $@ $(SOURCES) $(INCL) $(FLAGS)

clean:
	rm $(EXECUTABLE)
	rm *.o
setenv:
	echo "setenv PATH /opt/pccts/bin/:$$PATH"
