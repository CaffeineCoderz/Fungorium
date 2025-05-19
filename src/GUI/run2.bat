@echo off
cd..
cd..
:: Compile
javac -d out src\main.java src\GUI\*.java src\utils\*.java src\model\fungus\*.java src\GUI\Views\*.java src\model\insect\*.java src\model\interfaces\*.java src\model\sporeTypes\*.java src\model\tektonTypes\*.java src\logic\*.java src\commands\*.java

:: Run
java -cp out main