#!/bin/bash
# Kimeneti könyvtár törlése
rm -rf out

# Fordítás
mkdir -p out
javac -d out test/Tests.java src/pkg/main.java src/pkg/utils/*.java src/pkg/model/fungus/*.java src/pkg/model/interfaces/*.java src/pkg/model/sporeTypes/*.java src/pkg/model/tektonTypes/*.java src/pkg/model/insect/*.java src/pkg/logic/*.java

# Futtatás
java -cp out test.Tests
