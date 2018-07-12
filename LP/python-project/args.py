#!/usr/bin/python3

import argparse

parser = argparse.ArgumentParser(prog='cerca')
parser.add_argument('-k', '--key', nargs=1, help='search for certain words')
parser.add_argument('-d', '--date', nargs=1,
                    help='limit your search to certain dates')

args = parser.parse_args()
ayy = vars(args)
print (vars(args))
