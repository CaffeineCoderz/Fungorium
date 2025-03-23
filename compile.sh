mkdir -p out
javac -d out src/pkg/main.java src/pkg/utils/*.java src/pkg/model/fungus/*.java src/pkg/model/interfaces/*.java src/pkg/model/sporeTypes/*.java src/pkg/model/tektonTypes/*.java src/pkg/model/insect/*.java src/pkg/logic/*.java
echo "Fordítás kész az új!"