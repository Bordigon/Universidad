#!/bin/bash

# $1 será la asignatura, y $2 será la carpeta y el archivo que querramos ejecutar
# 1 = base_de_datos, 2 = concurrencia, 3 = cursos (JUnit)

case "$1" in
  1)
    java -cp "bin:lib/cclib.jar:lib/mysql-connector-j-9.6.0.jar" entregas.base_de_datos.$2
    ;;
  2)
    java -cp "bin:lib/cclib.jar:lib/mysql-connector-j-9.6.0.jar" entregas.concurrencia.$2
    ;;
  3)
    java -jar lib/junit-platform-console-standalone-1.9.2.jar \
      --class-path "bin:lib/mysql-connector-j-9.6.0.jar" \
      --select-class cursos.MainS4
    ;;
  *)
    echo "Uso: $0 <asignatura> <clase>"
    echo "  1 = base_de_datos  (ej: $0 1 p1.MainS1)"
    echo "  2 = concurrencia   (ej: $0 2 p1.CC_01_Threads)"
    echo "  3 = cursos JUnit   (no requiere segundo argumento)"
    exit 1
    ;;
esac
