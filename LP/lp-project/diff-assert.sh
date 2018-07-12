cat $1 | $2 > out.txt
diff -w out.txt $1.res
