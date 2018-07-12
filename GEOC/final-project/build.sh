#!/bin/bash
tar caf source.tar.gz index.html main.js
pandoc README.md -o description.pdf
