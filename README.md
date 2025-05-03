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

## Run && Compile at the same time:
#### 3. Run && Compile:
```sh
chmod +x runcompile.sh
./runcompile.sh
```
## Compile

#### 4. compile commandok futtatása:
```sh
cd C:\Users\cloud\Downloads\Fungorium

javac -d out -sourcepath src  src/main.java src/utils/*.java src/model/fungus/*.java src/model/insect/*.java src/model/interfaces/*.java src/model/sporeTypes/*.java src/model/tektonTypes/*.java src/logic/*.java src/commands/*.java
cd out
jar cvef main .\proto.jar main.class utils/*.class fungus/*.class insect/*.class interfaces/*.class sporeTypes/*.class tektonTypes/*.class logic/*.class commands/*.class
cd ..
java -jar out/proto.jar

```
