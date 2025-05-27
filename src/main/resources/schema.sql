-- Tabla para Competencias del Programa (Simulada, para referencia)
-- Si este módulo fuera desarrollado, tendría más campos y relaciones.
CREATE TABLE IF NOT EXISTS programa_competencias (
    id VARCHAR(50) PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    nivel VARCHAR(50) NOT NULL
);

-- Tabla para Resultados de Aprendizaje del Programa (Simulada, para referencia)
CREATE TABLE IF NOT EXISTS programa_ras (
    id VARCHAR(50) PRIMARY KEY,
    descripcion VARCHAR(500) NOT NULL,
    competencia_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (competencia_id) REFERENCES programa_competencias(id)
);

-- Tabla para Asignaturas (Simulada, para referencia)
CREATE TABLE IF NOT EXISTS asignaturas (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(500),
    creditos INT,
    semestre INT
);

-- Tabla para Docentes (Simulada, para referencia)
CREATE TABLE IF NOT EXISTS docentes (
    id VARCHAR(50) PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    tipo_identificacion VARCHAR(50),
    identificacion VARCHAR(50) UNIQUE NOT NULL,
    tipo_docente VARCHAR(50),
    correo_institucional VARCHAR(255) UNIQUE NOT NULL,
    ultimo_titulo VARCHAR(255)
);

-- MODULOS OBLIGATORIOS A DESARROLLAR:
-- Tabla para Resultados de Aprendizaje por Asignatura
CREATE TABLE IF NOT EXISTS asignatura_ras (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asignatura_id VARCHAR(50) NOT NULL,
    docente_id VARCHAR(50) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    semestre_academico VARCHAR(50) NOT NULL, -- Ej: 2024-1, 2024-2
    -- Opcional: Referencia al RAP del programa si se desea un vínculo fuerte desde la DB
    programa_ra_id VARCHAR(50),
    FOREIGN KEY (asignatura_id) REFERENCES asignaturas(id),
    FOREIGN KEY (docente_id) REFERENCES docentes(id),
    FOREIGN KEY (programa_ra_id) REFERENCES programa_ras(id)
);

-- Tabla para Rúbricas de Evaluación
CREATE TABLE IF NOT EXISTS rubricas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    asignatura_ra_id BIGINT NOT NULL,
    FOREIGN KEY (asignatura_ra_id) REFERENCES asignatura_ras(id)
);

-- Tabla para Criterios de Evaluación dentro de una Rúbrica
CREATE TABLE IF NOT EXISTS criterios_evaluacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rubrica_id BIGINT NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    ponderacion DECIMAL(5,2) NOT NULL, -- Porcentaje (ej: 0.20, 0.30)
    FOREIGN KEY (rubrica_id) REFERENCES rubricas(id)
);

-- Tabla para Niveles de Desempeño dentro de un Criterio
CREATE TABLE IF NOT EXISTS niveles_desempeno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    criterio_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL, -- Ej: Excelente, Bueno, Regular, Deficiente
    puntuacion DECIMAL(5,2) NOT NULL, -- Puntuación numérica para este nivel (Ej: 5.0, 4.0)
    descripcion VARCHAR(1000) NOT NULL,
    FOREIGN KEY (criterio_id) REFERENCES criterios_evaluacion(id)
);

-- Tabla para Evaluaciones de Estudiantes por Rúbrica
CREATE TABLE IF NOT EXISTS evaluaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rubrica_id BIGINT NOT NULL,
    estudiante_id VARCHAR(50) NOT NULL, -- Asumimos un ID de estudiante, que podría venir de otro sistema
    evaluador_id VARCHAR(50) NOT NULL, -- ID del docente o evaluador externo
    fecha_evaluacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    puntuacion_total DECIMAL(5,2) NOT NULL,
    retroalimentacion TEXT,
    FOREIGN KEY (rubrica_id) REFERENCES rubricas(id)
    -- Asumimos que estudiante_id y evaluador_id se referencian a un sistema externo
    -- O podríamos crear una tabla de estudiantes para este propósito de prueba
);

-- Tabla para Almacenar las selecciones de niveles por criterio en una evaluación
CREATE TABLE IF NOT EXISTS evaluacion_detalles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evaluacion_id BIGINT NOT NULL,
    criterio_id BIGINT NOT NULL,
    nivel_seleccionado_id BIGINT NOT NULL,
    FOREIGN KEY (evaluacion_id) REFERENCES evaluaciones(id),
    FOREIGN KEY (criterio_id) REFERENCES criterios_evaluacion(id),
    FOREIGN KEY (nivel_seleccionado_id) REFERENCES niveles_desempeno(id)
);

-- Tabla para Roles de Usuario (Si quieres manejar roles en la DB para el login)
CREATE TABLE IF NOT EXISTS roles (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

-- Tabla para Usuarios (Coordinador, Docente, Evaluador Externo)
CREATE TABLE IF NOT EXISTS usuarios (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    correo VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- Almacenar hash de la contraseña
    rol_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);