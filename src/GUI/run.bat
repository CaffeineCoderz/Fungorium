cd ..
cd ..
javac -d out -sourcepath src src/GUI/*.java src/main.java src/utils/*.java src/model/fungus/*.java src/GUI/Views/*.java src/model/insect/*.java src/model/interfaces/*.java src/model/sporeTypes/*.java src/model/tektonTypes/*.java src/logic/*.java src/commands/*.java
cd out
jar cvef main .\proto.jar main.class utils/*.class fungus/*.class insect/*.class interfaces/*.class sporeTypes/*.class tektonTypes/*.class logic/*.class commands/*.class GUI/*.class GUI/Views/*.class
cd ..
java -jar out/proto.jar
pause