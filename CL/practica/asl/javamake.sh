#!/bin/sh

antlr4 Asl.g4
javac *.java
grun Asl program -gui t.expr
