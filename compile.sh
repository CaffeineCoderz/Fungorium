mkdir -p out
javac -d out src/main.java src/utils/*.java src/model/fungus/*.java src/model/interfaces/*.java src/model/sporeTypes/*.java src/model/tektonTypes/*.java src/model/insect/*.java src/logic/*.java src/commands/*.java src/GUI/*.java
echo "Fordítás kész az új!"