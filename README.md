# Fungorium
A projlab fungorium feladata

## Program futtatása 

#### 1. Fordításra a konzolba írd be: 
```sh
chmod +x run.sh
./run.sh
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

#### 4. Specifikus tesztek futtatása:
```sh
java -jar lib/junit-platform-console-standalone-1.10.0.jar --classpath bin -c main.FungoriumTest
```

Junit downloaded with:
```sh
mkdir -p lib
curl -o lib/junit-platform-console-standalone-1.10.0.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.0/junit-platform-console-standalone-1.10.0.jar
```