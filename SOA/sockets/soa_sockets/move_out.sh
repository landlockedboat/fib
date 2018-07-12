#!/bin/bash
TESTPATH=tests/test_$1/
mkdir $TESTPATH
mv client_* $TESTPATH
mv launch_info $TESTPATH
mv launchClient.log $TESTPATH
