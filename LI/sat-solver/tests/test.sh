# !/bin/bash
TMP=res.tmp
EXEC=SAT.out

if [ -f $TMP ] ; then
    rm $TMP
fi

for V in 100 150 200 250 300
do
  for N in {1..10}
  do
    echo "Executing program with input set $N with $V variables:"
    time cat tests/entries/vars-$V-$N.cnf | ./$EXEC >> $TMP
    echo "Done."
    echo ""
  done
  DIFF=$(diff $TMP tests/tests-$V.res)
  if [ "$DIFF" != "" ]
  then
    echo "Test for $V variables failed:"
    echo "$DIFF"
    exit 1
  fi
  rm $TMP
done
