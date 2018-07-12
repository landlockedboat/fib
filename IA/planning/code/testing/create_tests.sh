#! /bin/bash
COUNTER=0
while [ $COUNTER -lt 50 ]; do
	./crea > entrada_$COUNTER.pddl
	let COUNTER+=1
done
