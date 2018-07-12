#!/bin/bash
p=0
t=0
time=0
usage="Usage: BadUser.sh [-p | -t time]"
# detecció de opcions d'entrada: només son vàlids: sense paràmetres i -p
if [ $# -ne 0 ]; then
    if  [ $# -eq 1 ]; then
        if [ $1 == "-p" ]; then 
            p=1
        else
            echo $usage; exit 1
        fi 
    fi
    if  [ $# -eq 2 ]; then
		if [ $1 == "-t" ]; then
			t=1
			time=$2
			lastchr=`echo $time | rev | cut -c 1`
			time=`echo $time | rev | cut -c 2- | rev`
			if [ $lastchr == "m" ]; then
				time=$(($time * 30))
			fi
		else
			echo $usage; exit 1
    	fi
    else 
        echo $usage; exit 1
    fi
fi

# tractar param -t
if [ $t -eq 1 ]; then
	lastlog=`last +$time | cut -d " " -f 1 | head -n -2`
	for user in $lastlog; do
		mod_files=`find / -user $user -mtime +$time`
		if [ -z "$mod_files" ]; then
			echo $user
		fi
	done

	exit 0
fi


# afegiu una comanda per llegir el fitxer de password i només agafar el camp de # nom de l'usuari
USERS=`cat /etc/passwd | cut -d : -f 1`
for user in $USERS; do
    home=`cat /etc/passwd | grep "^$user\>" | cut -d: -f 6`
    if [ -d $home ]; then
        num_fich=`find $home -type f -user $user | wc -l`
    else
        num_fich=0
    fi
    if [ $num_fich -eq 0 ] ; then
        if [ $p -eq 1 ]; then
# afegiu una comanda per detectar si l'usuari te processos en execució, 
# si no te ningú la variable $user_proc ha de ser 0
			user_proc=`find $home -type f -user $user | wc -l`
            if [ $user_proc -eq 0 ]; then
                echo "$user"
            fi
        else
            echo "$user"
        fi
    fi    
done
