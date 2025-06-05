-- Datos de roles
INSERT INTO roles (id, nombre) VALUES ('ROL_COORDINADOR', 'Coordinador');
INSERT INTO roles (id, nombre) VALUES ('ROL_DOCENTE', 'Docente');
INSERT INTO roles (id, nombre) VALUES ('ROL_EVALUADOR_EXTERNO', 'Evaluador Externo');
INSERT INTO roles (id, nombre) VALUES ('ROL_ESTUDIANTE', 'Estudiante'); -- Agregamos rol para estudiantes

-- Datos de usuarios de prueba (contraseñas codificadas con BCrypt: "password")
-- Por favor, reemplaza 'TU_HASH_BCRYPT_PARA_PASSWORD' con el hash real generado para "password".
-- Por ejemplo: $2a$10$Q7iM0y3sB8w9uC1k2jL5.oXq3p4r5t6u7v8w9x0y1z2A3B4C5D6E7F8G9H0
INSERT INTO usuarios (nombre, apellido, correo, password, rol_id) VALUES
('Ana', 'García', 'ana.garcia@unicauca.edu.co', 'TU_HASH_BCRYPT_PARA_PASSWORD', 'ROL_COORDINADOR'),
('Carlos', 'Pérez', 'carlos.perez@unicauca.edu.co', 'TU_HASH_BCRYPT_PARA_PASSWORD', 'ROL_DOCENTE'),
('Laura', 'Martínez', 'laura.martinez@evaluador.com', 'TU_HASH_BCRYPT_PARA_PASSWORD', 'ROL_EVALUADOR_EXTERNO'),
('Juan', 'Guerrero', 'juan.guerrero@unicauca.edu.co', 'TU_HASH_BCRYPT_PARA_PASSWORD', 'ROL_ESTUDIANTE'),
('Maria', 'Lopez', 'maria.lopez.est@unicauca.edu.co', 'TU_HASH_BCRYPT_PARA_PASSWORD', 'ROL_ESTUDIANTE');


-- Datos para docentes (IDs deben coincidir con los de usuarios, ya que docentes.id es FK a usuarios.id)
-- Asumimos que los IDs autogenerados para los usuarios anteriores son 1, 2, 3, 4, 5.
INSERT INTO docentes (id, nombres, apellidos, tipo_identificacion, identificacion, tipo_docente, correo_institucional, ultimo_titulo) VALUES
(2, 'Carlos', 'Pérez', 'CC', '123456789', 'Planta', 'carlos.perez@unicauca.edu.co', 'PhD en Ciencias de la Computación'),
(3, 'Laura', 'Martínez', 'CC', '987654321', 'Catedra', 'laura.martinez@evaluador.com', 'Magíster en Ingeniería de Sistemas');


-- Datos simulados para modulos no desarrollados
INSERT INTO programa_competencias (id, descripcion, nivel) VALUES
('COMP001', 'Diseñar soluciones de software', 'Avanzado'),
('COMP002', 'Aplicar metodologías ágiles', 'Intermedio');

INSERT INTO programa_ras (id, descripcion, competencia_id) VALUES
('RAP001', 'El estudiante diseña software utilizando metodologías ágiles', 'COMP001'),
('RAP002', 'El estudiante implementa pruebas unitarias y de integración', 'COMP002');

INSERT INTO asignaturas (id, nombre, descripcion, creditos, semestre) VALUES
('ISIII', 'Ingeniería de Software III', 'Tópicos avanzados de desarrollo de software', 4, 6),
('BDII', 'Bases de Datos II', 'Gestión de bases de datos avanzadas', 3, 5);


-- Datos de prueba para modulos OBLIGATORIOS:
-- Asignatura RA (Docente Carlos Pérez, Asignatura ISIII)
-- Los IDs de 'asignatura_ras' se auto-generan (1, 2, ...)
INSERT INTO asignatura_ras (asignatura_id, docente_id, descripcion, semestre_academico, programa_ra_id) VALUES
('ISIII', 2, 'El estudiante aplica principios de diseño de software para sistemas escalables.', '2025-1', 'RAP001'), -- Carlos Pérez (id 2)
('ISIII', 2, 'El estudiante implementa patrones de diseño en el desarrollo de aplicaciones web.', '2025-1', 'RAP001');

-- Rúbricas para RA (asociadas a asignatura_ra_id auto-generados 1 y 2)
-- Los IDs de 'rubricas' se auto-generan (1, 2, ...)
INSERT INTO rubricas (nombre, asignatura_ra_id) VALUES
('Rúbrica de Diseño de Arquitectura', 1), -- Asociada al primer asignatura_ras
('Rúbrica de Patrones de Diseño', 2);     -- Asociada al segundo asignatura_ras

-- Criterios y Niveles para Rúbrica 1 (Rúbrica de Diseño de Arquitectura)
-- Los IDs de 'criterios_evaluacion' se auto-generan (1, 2, 3...)
INSERT INTO criterios_evaluacion (rubrica_id, descripcion, ponderacion) VALUES
(1, 'Calidad del Diseño de Arquitectura', 0.40), -- Asociado a la primera rúbrica
(1, 'Coherencia con Requisitos', 0.30),
(1, 'Documentación del Diseño', 0.30);

-- Los IDs de 'niveles_desempeno' se auto-generan (1, 2, ...)
INSERT INTO niveles_desempeno (criterio_id, nombre, puntuacion, descripcion) VALUES
(1, 'Excelente', 5.0, 'El diseño de arquitectura es óptimo, escalable y robusto.'),
(1, 'Bueno', 4.0, 'El diseño de arquitectura es sólido con mejoras menores posibles.'),
(1, 'Regular', 3.0, 'El diseño de arquitectura presenta deficiencias significativas.'),
(1, 'Deficiente', 2.0, 'El diseño de arquitectura es inadecuado o inexistente.'),

(2, 'Excelente', 5.0, 'El diseño de arquitectura cumple totalmente con los requisitos funcionales y no funcionales.'),
(2, 'Bueno', 4.0, 'El diseño de arquitectura cumple la mayoría de los requisitos, con algunas omisiones menores.'),
(2, 'Regular', 3.0, 'El diseño de arquitectura omite varios requisitos importantes.'),
(2, 'Deficiente', 2.0, 'El diseño de arquitectura no aborda los requisitos clave.'),

(3, 'Excelente', 5.0, 'La documentación del diseño es clara, completa y fácil de entender.'),
(3, 'Bueno', 4.0, 'La documentación del diseño es adecuada, con algunas áreas que podrían mejorarse.'),
(3, 'Regular', 3.0, 'La documentación del diseño es incompleta o poco clara.'),
(3, 'Deficiente', 2.0, 'La documentación del diseño es inexistente o inútil.');

-- Criterios y Niveles para Rúbrica 2 (Rúbrica de Patrones de Diseño)
-- Los IDs de 'criterios_evaluacion' continúan auto-generándose (4, 5...)
INSERT INTO criterios_evaluacion (rubrica_id, descripcion, ponderacion) VALUES
(2, 'Aplicación de Patrones', 0.50), -- Asociado a la segunda rúbrica
(2, 'Claridad y Cohesión del Código', 0.50);

-- Los IDs de 'niveles_desempeno' continúan auto-generándose (13, 14...)
INSERT INTO niveles_desempeno (criterio_id, nombre, puntuacion, descripcion) VALUES
(4, 'Excelente', 5.0, 'Patrones de diseño aplicados de forma óptima, mejorando la modularidad y extensibilidad.'),
(4, 'Bueno', 4.0, 'Patrones de diseño aplicados correctamente, con oportunidades de mejora en su uso.'),
(4, 'Regular', 3.0, 'Aplicación inconsistente o incorrecta de patrones de diseño.'),
(4, 'Deficiente', 2.0, 'Ausencia o uso incorrecto de patrones de diseño.'),

(5, 'Excelente', 5.0, 'Código limpio, modular, altamente legible y mantenible.'),
(5, 'Bueno', 4.0, 'Código con buena estructura, aunque con algunas complejidades menores.'),
(5, 'Regular', 3.0, 'Código con algunas áreas confusas o poco cohesivas.'),
(5, 'Deficiente', 2.0, 'Código difícil de leer, con baja cohesión y alto acoplamiento.');


-- Datos de una evaluación de ejemplo
-- Los IDs de 'evaluaciones' se auto-generan (1, 2, ...)
-- evaluador_id: 2 (Carlos Pérez, Docente), estudiante_id: 4 (Juan Guerrero)
-- rubrica_id: 1 (Rúbrica de Diseño de Arquitectura)
INSERT INTO evaluaciones (rubrica_id, estudiante_id, evaluador_id, fecha_evaluacion, puntuacion_total, retroalimentacion) VALUES
(1, 4, 2, CURRENT_TIMESTAMP, 4.25, 'Buen esfuerzo, la documentación podría ser más detallada.');

-- Detalles de la evaluación (asociando con criterios de la rúbrica 1)
-- Asumimos criterio_id 1 (Calidad del Diseño), nivel_seleccionado_id 2 (Bueno)
-- Asumimos criterio_id 2 (Coherencia con Requisitos), nivel_seleccionado_id 5 (Excelente)
-- Asumimos criterio_id 3 (Documentación del Diseño), nivel_seleccionado_id 11 (Regular)
INSERT INTO evaluacion_detalles (evaluacion_id, criterio_id, nivel_seleccionado_id) VALUES
(1, 1, 2), -- Eval 1, Crit 1 (Calidad Diseño), Nivel 2 (Bueno)
(1, 2, 5), -- Eval 1, Crit 2 (Coherencia), Nivel 5 (Excelente)
(1, 3, 11); -- Eval 1, Crit 3 (Documentación), Nivel 11 (Regular)
