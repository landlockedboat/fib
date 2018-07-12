# setup
cat /assig/cl/tcshrc.CL-GRAU.antlr4 >> ~/.tcshrc
source ~/.tcshrc

antlr4 Calc.g4
javac *java

# fancy GUI
grun Calc prog -gui t.expr
# text
grun Calc prog -tree t.expr

