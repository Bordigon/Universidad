@echo off

::%1 será la asignatura, y %2 será la carpeta y el archivo que querramos ejecutar

if "%1"=="1" goto base_de_datos
if "%1"=="2" goto concurrencia
if "%1"=="3" goto cursos

:base_de_datos
java -cp bin;lib/cclib.jar;lib/mysql-connector-j-9.6.0.jar entregas.base_de_datos.%2
exit /b

:concurrencia
java -cp bin;lib/cclib.jar;lib/mysql-connector-j-9.6.0.jar entregas.concurrencia.%2
exit /b

:cursos
java -jar lib/junit-platform-console-standalone-1.9.2.jar --class-path "bin;lib/mysql-connector-j-9.6.0.jar" --select-class cursos.MainS4



