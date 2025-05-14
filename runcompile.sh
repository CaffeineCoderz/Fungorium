#!/bin/bash

# Töröljük az előző fordítás eredményét
rm -rf out

# Újra létrehozzuk az out mappát
mkdir -p out

# Fordítás
javac -d out src/main.java src/utils/*.java src/model/fungus/*.java src/model/interfaces/*.java src/model/sporeTypes/*.java src/model/tektonTypes/*.java src/model/insect/*.java src/logic/*.java src/commands/*.java src/GUI/*.java src/GUI/Views/*.java  
echo "Fordítás kész az új!"

# Futtatás
java -cp out main