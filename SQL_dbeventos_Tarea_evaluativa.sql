-- Creación de la base de datos
CREATE DATABASE dbeventos;
USE dbeventos;

-- Creación de la tabla 'ubicaciones'
CREATE TABLE ubicaciones (
    id_ubicacion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    capacidad INT NOT NULL
);

-- Creación de la tabla 'eventos'
CREATE TABLE eventos (
    id_evento INT AUTO_INCREMENT PRIMARY KEY,
    nombre_evento VARCHAR(255) NOT NULL,
    fecha DATE NOT NULL,
    id_ubicacion INT,
    FOREIGN KEY (id_ubicacion) REFERENCES ubicaciones(id_ubicacion)
);

-- Creación de la tabla 'asistentes'
CREATE TABLE asistentes (
    dni VARCHAR(10) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL
);

-- Creamos una tabla intermedia para relacionar asistentes con eventos
CREATE TABLE asistentes_eventos (
    dni VARCHAR(10),
    id_evento INT,
    PRIMARY KEY (dni, id_evento),
    FOREIGN KEY (dni) REFERENCES asistentes(dni),
    FOREIGN KEY (id_evento) REFERENCES eventos(id_evento)
);

INSERT INTO ubicaciones (nombre, direccion, capacidad) VALUES 
('Teatro Nacional', 'Av. del Sol, 156', 500),
('Estadio Ciudad', 'Blvd. de los Heroes, 895', 15000),
('Centro de Convenciones Estrella', 'Calle Cosmos, 12', 2500),
('Auditorio Luna', 'Callejón Estrella Fugaz, 45', 750),
('Arena Cosmos', 'Ronda de Saturno, 984', 8000);


INSERT INTO eventos (nombre_evento, fecha, id_ubicacion) VALUES 
('Concierto RockSólido', '2023-11-05', 2),
('Obra "Un Sueño Distante"', '2023-11-10', 1),
('Conferencia Tecnológica 2023', '2023-11-15', 3),
('Gala de Ballet Estelar', '2023-11-20', 4),
('Torneo Nacional de Lucha', '2023-11-22', 5);


INSERT INTO asistentes (dni, nombre) VALUES 
('12345678A', 'Juan García'),
('23456789B', 'María Fernandez'),
('34567890C', 'Luis Rodríguez'),
('45678901D', 'Ana Pérez'),
('56789012E', 'Pedro Martínez'),
('67890123F', 'Sofía González'),
('78901234G', 'Carlos Ramírez'),
('89012345H', 'Elena Torres'),
('90123456I', 'Miguel Sanz'),
('01234567J', 'Laura Gutierrez'),
('11223344K', 'Roberto Álvarez'),
('22334455L', 'Isabel Quintana'),
('33445566M', 'Gabriel Ortiz'),
('44556677N', 'Carmen Delgado'),
('55667788O', 'Fernando Mendoza'),
('66778899P', 'Teresa Ríos'),
('77889900Q', 'Ricardo Vargas'),
('88990011R', 'Julia Vázquez'),
('99001122S', 'Enrique Castillo'),
('00112233T', 'Rosa Herrera');


INSERT INTO asistentes_eventos (dni, id_evento) VALUES 
('12345678A', 1),
('23456789B', 1),
('34567890C', 2),
('45678901D', 2),
('56789012E', 3),
('67890123F', 3),
('78901234G', 4),
('89012345H', 4),
('90123456I', 5),
('01234567J', 5),
('11223344K', 1),
('22334455L', 2),
('33445566M', 1),
('44556677N', 3),
('55667788O', 4),
('66778899P', 5),
('77889900Q', 3),
('88990011R', 2),
('99001122S', 1),
('00112233T', 5),
('11223344K', 3),
('22334455L', 4),
('33445566M', 5),
('44556677N', 1),
('55667788O', 2);

/*función programa 4*/

DELIMITER $$

CREATE FUNCTION obtener_numero_asistentes(id_evento INT) 
RETURNS INT DETERMINISTIC
BEGIN
    DECLARE num_asistentes INT;
    
    SELECT COUNT(*) INTO num_asistentes
    FROM asistentes_eventos
    WHERE id_evento = id_evento;
    
    RETURN num_asistentes;
END $$

DELIMITER ;