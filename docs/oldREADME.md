# Fungorium
A projlab fungorium feladata

## Program futtatása 

#### 1. Fordításra a konzolba írd be: 
```sh
chmod +x compile.sh
./compile.sh
```

#### 2. Futtatáshoz a konzolba írd be: 
```sh
chmod +x run.sh
./run.sh
```

## Tesztek

#### 3. Tesztek futtatása:
```sh
chmod +x test.sh
./test.sh
```
## Compile

#### 4. compile commandok futtatása:
```sh
cd C:\Users\cloud\Downloads\Fungorium

javac -d out -sourcepath src  src/main.java test/Tests.java src/utils/*.java src/model/fungus/*.java src/model/insect/*.java src/model/interfaces/*.java src/model/sporeTypes/*.java src/model/tektonTypes/*.java src/logic/*.java

cd C:\Users\cloud\Downloads\Fungorium\out

java -jar cvef test.Tests .\szkeleton.jar main.class test/Tests.class utils/*.class fungus/*.class insect/*.class interfaces/*.class sporeTypes/*.class tektonTypes/*.class logic/*.class
java -jar szkeleton.jar

```
