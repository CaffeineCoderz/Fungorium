cd ..
cd ..
javac -d out -sourcepath src src/GUI/*.java
cd out
jar cvef GUI/GUITest .\szkeleton.jar GUI/*.class
cd ..
java -jar out/szkeleton.jar