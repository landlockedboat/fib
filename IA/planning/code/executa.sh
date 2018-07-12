#! /bin/bash
for i in $( ls entrades/); do
	/home/raluca/Downloads/Metric-FF/ff -f entrades/$i -o domain.pddl > sortides/sortida$i.txt
done
