#!/bin/bash

# Fordítás
mkdir -p out
javac -d out -cp "lib/*:out" src/test/*.java src/main/*.java

# Tesztek futtatása
java -jar lib/junit-platform-console-standalone-1.10.0.jar --class-path out --scan-class-path
