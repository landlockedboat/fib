#!/bin/bash

DIFF="diff"

set -e

echo ""
echo "BEGIN examples-full/typecheck"
for f in ../examples/jp_chkt_*.asl; do
    echo $(basename $f)
    ./asl $f | egrep ^L > tmp.err
    $DIFF tmp.err ${f/asl/err}
    rm -f tmp.err
done
echo "END   examples-full/typecheck"

echo ""
echo "BEGIN examples-full/execution"
for f in ../examples/jp_genc_*.asl; do
    echo $(basename "$f")
    ./asl "$f" > tmp.t
    ../tvm/tvm tmp.t < "${f/asl/in}" > tmp.out
    $DIFF tmp.out "${f/asl/out}"
    rm -f tmp.t tmp.out
done
echo "END   examples-full/execution"
