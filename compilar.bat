@echo off

:: %1 será la asignatura, y %2 será la carpeta donde estén los archivos a compilar


if "%1"=="1" goto base_de_datos
if "%1"=="2" goto concurrencia
if "%1"=="3" goto other

:base_de_datos
javac -d bin -cp "lib/*;bin" src/base_de_datos/%2/*.java
exit /b

:concurrencia
javac -d bin -cp "lib/*;bin" src/concurrencia/%2/*.java
exit /b

:other
javac -d bin -cp "lib/*;bin" src/%2/*.java
