#!/bin/bash

if ! [ -f ./core.int ]; then
	echo "ERROR: Missing core.int"
	exit 1
fi
if ! [ -f ./tbc.c ]; then
	echo "ERROR: Missing tbc.c"
	exit 1
fi

if ! [ -f ./tbc ]; then
	gcc -o tbc tbc.c
fi

# If arg size is not 2, error
if [ $# -ne 2 ] || ! [ -f "$1" ]; then
	echo "USAGE: compile.sh <source file> <output file>"
	exit 1
fi

# Transpile the Befunge-93 source code
./tbc "$1" > "$2.tmp.c"

# Compile the C source code
gcc "$2.tmp.c" -o "$2"

# Remove the C source code
rm "$2.tmp.c"
