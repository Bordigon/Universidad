/* =========================================================
   CREACIÓN DE BASE DE DATOS
   ========================================================= */

DROP DATABASE IF EXISTS cursos_db;
CREATE DATABASE cursos_db;
USE cursos_db;


/* =========================================================
   TABLA: curso
   ========================================================= */

CREATE TABLE curso (
    id              INT PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    descripcion     VARCHAR(255),
    fecha_inicio    DATE NOT NULL
);


/* =========================================================
   TABLA: modulo (entidad débil)
   ========================================================= */

CREATE TABLE modulo (
    curso_id        INT NOT NULL,
    n_modulo        INT NOT NULL,
    titulo          VARCHAR(150) NOT NULL,
    horas           INT NOT NULL,
    PRIMARY KEY (curso_id, n_modulo),
    FOREIGN KEY (curso_id) REFERENCES curso(id)
        ON DELETE CASCADE,
    CHECK (horas > 0),
    CHECK (n_modulo > 0)
);


/* =========================================================
   TABLA: direccion
   ========================================================= */

CREATE TABLE direccion (
    id              INT PRIMARY KEY,
    tipo_via        VARCHAR(30) NOT NULL,
    nombre_via      VARCHAR(120) NOT NULL,
    municipio       VARCHAR(100) NOT NULL
);


/* =========================================================
   TABLA: profesor (sin fotografía)
   ========================================================= */

CREATE TABLE profesor (
    id              INT PRIMARY KEY,
    nombre          VARCHAR(80) NOT NULL,
    apellido1       VARCHAR(80) NOT NULL,
    apellido2       VARCHAR(80),
    direccion_id    INT UNIQUE,
    FOREIGN KEY (direccion_id) REFERENCES direccion(id)
        ON DELETE SET NULL
);


/* =========================================================
   TABLA: edificio
   ========================================================= */

CREATE TABLE edificio (
    id              INT PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL
);


/* =========================================================
   TABLA: aula
   ========================================================= */

CREATE TABLE aula (
    id              INT PRIMARY KEY,
    descripcion     VARCHAR(255),
    capacidad       INT NOT NULL,
    edificio_id     INT NULL,
    FOREIGN KEY (edificio_id) REFERENCES edificio(id)
        ON DELETE SET NULL,
    CHECK (capacidad > 0)
);

/* =========================================================
   INSERTS SINTÉTICOS
   ========================================================= */


/* =========================
   CURSOS
   ========================= */

INSERT INTO curso VALUES
(1, 'Desarrollo Web', 'Frontend, backend y despliegue', '2025-01-15'),
(2, 'Administración de Sistemas', 'Linux, redes y seguridad', '2025-02-01'),
(3, 'Bases de Datos', 'Modelado y SQL', '2025-03-10'),
(4, 'Inteligencia Artificial', 'IA y Machine Learning', '2025-04-05'),
(5, 'Ofimática', 'Herramientas básicas', '2025-05-20'),
(6, 'Curso Vacío', 'Sin módulos aún', '2025-09-01');


/* =========================
   MÓDULOS
   ========================= */

INSERT INTO modulo VALUES
(1, 1, 'HTML y CSS', 30),
(1, 2, 'JavaScript', 45),
(1, 3, 'Node.js', 50),
(1, 4, 'DevOps', 20),

(2, 1, 'Linux', 40),
(2, 2, 'Redes', 35),
(2, 3, 'Seguridad', 25),

(3, 1, 'Modelo ER', 20),
(3, 2, 'SQL básico', 30),
(3, 3, 'SQL avanzado', 35),
(3, 4, 'Optimización', 25),

(4, 1, 'Introducción IA', 15),
(4, 2, 'Machine Learning', 45),
(4, 3, 'Deep Learning', 50),

(5, 1, 'Word', 20),
(5, 2, 'Excel', 25);


/* =========================
   DIRECCIONES
   ========================= */

INSERT INTO direccion VALUES
(1, 'Calle', 'Gran Vía 12', 'Madrid'),
(2, 'Avenida', 'Ilustración 8', 'Sevilla'),
(3, 'Plaza', 'Mayor 3', 'Valencia'),
(4, 'Calle', 'Rosal 22', 'Málaga'),
(5, 'Paseo', 'Prado 101', 'Madrid'),
(6, 'Calle', 'Luna 7', 'Bilbao'); -- sin usar


/* =========================
   PROFESORES
   ========================= */

INSERT INTO profesor VALUES
(1, 'Ana', 'Martínez', 'López', 1),
(2, 'Luis', 'García', 'Ruiz', 2),
(3, 'Marta', 'Sánchez', NULL, NULL),
(4, 'Carlos', 'Pérez', 'Navarro', 3),
(5, 'Elena', 'Torres', 'Gil', 4),
(6, 'Javier', 'Romero', 'Díaz', NULL),
(7, 'Lucía', 'Ortega', 'Mena', 5),
(8, 'Pedro', 'Jiménez', NULL, NULL);


/* =========================
   EDIFICIOS
   ========================= */

INSERT INTO edificio VALUES
(1, 'Edificio Central'),
(2, 'Edificio Norte'),
(3, 'Edificio Sur'),
(4, 'Edificio Vacío');


/* =========================
   AULAS
   ========================= */

INSERT INTO aula VALUES
(1, 'Informática 1', 30, 1),
(2, 'Informática 2', 25, 1),
(3, 'Laboratorio Redes', 20, 2),
(4, 'Sala Conferencias', 80, 1),
(5, 'Aula Polivalente', 35, 3),
(6, 'Aula Pequeña', 15, 3),
(7, 'Aula Provisional', 10, NULL), -- sin edificio
(8, 'Laboratorio IA', 22, 2);
