#!/bin/bash
usage="Usage: infouser.sh username"
user=""

if  [ $# -eq 1 ]; then
    user=$1
else 
    echo $usage; exit 1
fi

home=`cat /etc/passwd | grep "^$user\>" | cut -d: -f 6`
home_size=""

if [ -d $home ]; then
    home_size=`du -h $home | tail -1 | egrep -o "^.*[A-Z]"`
else
    echo "User $user does not have a home folder"
fi

echo "Home: $home"
echo "Home size: $home_size"

dirs_with_files=`find / -type f -user aso | grep -v "$home" | sed -r 's|/[^/]+$||' | uniq`

echo "Other dirs:"
echo "$dirs_with_files"

active_processes=`ps -u $user | wc -l`

echo "Active processes: $active_processes"
