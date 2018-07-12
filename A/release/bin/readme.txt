If you want to execute a test here, assuming UNIX shell, you can just

cat file.input | ./executable.out

And, if you want to get the execution time:

time cat file.input | ./executable.out
