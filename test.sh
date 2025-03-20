#!/bin/bash

# Kimeneti könyvtár törlése
rm -rf out

# Fordítás
mkdir -p out
javac -d out -cp "lib/*:out" src/test/*.java src/main/*.java

# Tesztek futtatása
# java -jar lib/junit-platform-console-standalone-1.10.0.jar --class-path out --scan-class-path

# Junit hozzáadása FungoriumTest.java teszthez
javac -cp "lib/junit-platform-console-standalone-1.10.0.jar;." -d out test/FungoriumTest.java
java -cp "lib/junit-platform-console-standalone-1.10.0.jar;out" org.junit.platform.console.ConsoleLauncher --select-class test.FungoriumTest

# Több Fájl esetén
# javac -cp "lib/junit-platform-console-standalone-1.10.0.jar;src" -d out src/test/*.java
# java -cp "lib/junit-platform-console-standalone-1.10.0.jar;out" org.junit.platform.console.ConsoleLauncher --scan-classpath