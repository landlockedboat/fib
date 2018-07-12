#!/bin/bash

FILES=`find / -type f -user $1 2> /dev/null`

# Make the backup
DIR=/home/backup/$1
test -d "$DIR" || mkdir -p "$DIR"
echo $FILES | xargs -d "\n" -i sudo cp --parents {} $DIR

# Erase all files
echo $FILES | xargs -d "\n" sudo rm -f

# Change the login shell for a tail script
chsh -s /usr/local/lib/no-login $1
