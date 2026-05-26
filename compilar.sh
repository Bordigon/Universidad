#!/bin/bash

# $1 será la asignatura, y $2 será la carpeta donde estén los archivos a compilar
# 1 = base_de_datos, 2 = concurrencia, 3 = other

case "$1" in
  1)
    javac -d bin -cp "lib/*:bin" src/base_de_datos/$2/*.java
    ;;
  2)
    javac -d bin -cp "lib/*:bin" src/concurrencia/$2/*.java
    ;;
  3)
    javac -d bin -cp "lib/*:bin" src/$2/*.java
    ;;
  *)
    echo "Uso: $0 <asignatura> <carpeta>"
    echo "  1 = base_de_datos"
    echo "  2 = concurrencia"
    echo "  3 = other (relativo a src/)"
    exit 1
    ;;
esac
