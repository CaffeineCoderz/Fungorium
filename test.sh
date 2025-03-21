#!/bin/bash
# Kimeneti könyvtár törlése
rm -rf out

# Fordítás
mkdir -p out
javac -d out test/Tests.java src/main.java src/utils/*.java src/model/fungus/*.java src/model/interfaces/*.java src/model/sporeTypes/*.java src/model/tektonTypes/*.java src/model/insect/*.java src/logic/*.java

# Futtatás
java -cp out test.Tests
