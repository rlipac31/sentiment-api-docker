CREATE TABLE usuarios
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    email          VARCHAR(100) UNIQUE,
    contrasenia    VARCHAR(100) NOT NULL,
    role           VARCHAR(20)  NOT NULL,
    fecha_registro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    state tinyint NOT NULL

);


CREATE TABLE comentarios
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    comentario   TEXT NOT NULL,
    prevision    ENUM('POSITIVO', 'NEGATIVO', 'NEUTRO') NULL,
    provabilidad DECIMAL(2, 1) NULL,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    state tinyint NOT NULL
);