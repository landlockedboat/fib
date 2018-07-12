#!/bin/bash

FOLDER=$1
TYPE="TOTAL"
LEVEL="0"
DATE=`date +%Y_%m_%d_%H_%M_%S`
FOLDERNAME="${FOLDER//\//-}"
NAME=$DATE\_$FOLDERNAME\_$TYPE\_$LEVEL
BACKUP_PATH="/backup"

tar -cvpf $BACKUP_PATH/$NAME.tar $FOLDER

md5sum $BACKUP_PATH/$NAME.tar | awk '{print $1}' > $BACKUP_PATH/$NAME.asc
