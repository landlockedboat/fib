#!/bin/bash
DIR=/home/backup/$1
test -d "$DIR" || mkdir -p "$DIR"
find / -type f -user $1 2> /dev/null | xargs -i cp --parents {} $DIR
