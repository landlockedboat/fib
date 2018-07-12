#!/bin/bash

DATE=`date +%Y_%m_%d_%H_%M_%S`
NAME=$DATE\_root\_INCREMENTAL\_1

sudo tar --newer=/backup/2017_12_14_15_35_47_-root-_TOTAL_0.tar -cvpf /backup/$NAME.tar /root

md5sum /backup/$NAME.tar | awk '{print $1}' > /backup/$NAME.asc
