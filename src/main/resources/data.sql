-- Datos de roles (Estos IDs no son auto-generados, se mantienen)
INSERT INTO roles (id, nombre) VALUES ('ROL_COORDINADOR', 'Coordinador');
INSERT INTO roles (id, nombre) VALUES ('ROL_DOCENTE', 'Docente');
INSERT INTO roles (id, nombre) VALUES ('ROL_EVALUADOR_EXTERNO', 'Evaluador Externo');

-- Datos de usuarios de prueba (contraseñas codificadas con BCrypt: "password")
-- ¡IMPORTANTE! Reemplaza 'TU_HASH_BCRYPT' con el hash real que generaste
-- Esto es crucial para que el login funcione correctamente con Spring Security
INSERT INTO usuarios (id, nombre, apellido, correo, password, rol_id) VALUES
('COORD001', 'Ana', 'García', 'ana.garcia@unicauca.edu.co', 'TU_HASH_BCRYPT', 'ROL_COORDINADOR'),
('DOCE001', 'Carlos', 'Pérez', 'carlos.perez@unicauca.edu.co', 'TU_HASH_BCRYPT', 'ROL_DOCENTE'),
('EVAL001', 'Laura', 'Martínez', 'laura.martinez@evaluador.com', 'TU_HASH_BCRYPT', 'ROL_EVALUADOR_EXTERNO');

-- Datos simulados para modulos no desarrollados (Estos IDs no son auto-generados, se mantienen)
INSERT INTO programa_competencias (id, descripcion, nivel) VALUES
('COMP001', 'Diseñar soluciones de software', 'Avanzado'),
('COMP002', 'Aplicar metodologías ágiles', 'Intermedio');

INSERT INTO programa_ras (id, descripcion, competencia_id) VALUES
('RAP001', 'El estudiante diseña software utilizando metodologías ágiles', 'COMP001'),
('RAP002', 'El estudiante implementa pruebas unitarias y de integración', 'COMP002');

INSERT INTO asignaturas (id, nombre, descripcion, creditos, semestre) VALUES
('ISIII', 'Ingeniería de Software III', 'Tópicos avanzados de desarrollo de software', 4, 6),
('BDII', 'Bases de Datos II', 'Gestión de bases de datos avanzadas', 3, 5);

INSERT INTO docentes (id, nombres, apellidos, tipo_identificacion, identificacion, tipo_docente, correo_institucional, ultimo_titulo) VALUES
('DOCE001', 'Carlos', 'Pérez', 'CC', '123456789', 'Planta', 'carlos.perez@unicauca.edu.co', 'PhD en Ciencias de la Computación'),
('DOCE002', 'María', 'López', 'CC', '987654321', 'Catedra', 'maria.lopez@unicauca.edu.co', 'Magíster en Ingeniería de Sistemas');

-- Datos de prueba para modulos OBLIGATORIOS:
-- Asignatura RA (Docente Carlos Pérez, Asignatura ISIII)
-- ¡EDITADO! Eliminado 'id' de los INSERTs para que H2 lo auto-genere
INSERT INTO asignatura_ras (asignatura_id, docente_id, descripcion, semestre_academico, programa_ra_id) VALUES
('ISIII', 'DOCE001', 'El estudiante aplica principios de diseño de software para sistemas escalables.', '2025-1', 'RAP001'),
('ISIII', 'DOCE001', 'El estudiante implementa patrones de diseño en el desarrollo de aplicaciones web.', '2025-1', 'RAP001');

-- Rúbricas para RA (asociadas a asignatura_ra_id 1)
-- ¡EDITADO! Eliminado 'id' de los INSERTs para que H2 lo auto-genere
-- NOTA: El asignatura_ra_id aquí (1 y 2) debe coincidir con los IDs auto-generados por las inserciones anteriores.
-- Esto puede ser problemático si el orden de inserción cambia o si tienes muchos datos.
-- Para pruebas simples funciona, pero para un sistema más robusto se usarían referencias dinámicas.
INSERT INTO rubricas (nombre, asignatura_ra_id) VALUES
('Rúbrica de Diseño de Arquitectura', 1), -- Asumiendo que el primer asignatura_ras insertado tiene ID 1
('Rúbrica de Patrones de Diseño', 2);   -- Asumiendo que el segundo asignatura_ras insertado tiene ID 2

-- Criterios y Niveles para Rúbrica 101 (Diseño de Arquitectura)
-- ¡EDITADO! Eliminado 'id' de los INSERTs para que H2 lo auto-genere
-- NOTA: Los rubrica_id (101 y 102) deben ser los IDs auto-generados para las rúbricas anteriores.
-- Esto también puede ser problemático. Considera usar un enfoque más robusto para relaciones.
INSERT INTO criterios_evaluacion (rubrica_id, descripcion, ponderacion) VALUES
(1, 'Calidad del Diseño de Arquitectura', 0.40), -- Asumiendo que la primera rúbrica insertada tiene ID 1
(1, 'Coherencia con Requisitos', 0.30),
(1, 'Documentación del Diseño', 0.30);

INSERT INTO niveles_desempeno (criterio_id, nombre, puntuacion, descripcion) VALUES
-- Asumiendo que los criterios de evaluación insertados tienen IDs 1, 2, 3 (en orden)
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

-- Criterios y Niveles para Rúbrica 102 (Patrones de Diseño)
-- ¡EDITADO! Eliminado 'id' de los INSERTs para que H2 lo auto-genere
INSERT INTO criterios_evaluacion (rubrica_id, descripcion, ponderacion) VALUES
(2, 'Aplicación de Patrones', 0.50), -- Asumiendo que la segunda rúbrica insertada tiene ID 2
(2, 'Claridad y Cohesión del Código', 0.50);

INSERT INTO niveles_desempeno (criterio_id, nombre, puntuacion, descripcion) VALUES
-- Asumiendo que los criterios de evaluación insertados tienen IDs 4, 5 (en orden, después de los anteriores)
(4, 'Excelente', 5.0, 'Patrones de diseño aplicados de forma óptima, mejorando la modularidad y extensibilidad.'),
(4, 'Bueno', 4.0, 'Patrones de diseño aplicados correctamente, con oportunidades de mejora en su uso.'),
(4, 'Regular', 3.0, 'Aplicación inconsistente o incorrecta de patrones de diseño.'),
(4, 'Deficiente', 2.0, 'Ausencia o uso incorrecto de patrones de diseño.'),

(5, 'Excelente', 5.0, 'Código limpio, modular, altamente legible y mantenible.'),
(5, 'Bueno', 4.0, 'Código con buena estructura, aunque con algunas complejidades menores.'),
(5, 'Regular', 3.0, 'Código con algunas áreas confusas o poco cohesivas.'),
(5, 'Deficiente', 2.0, 'Código difícil de leer, con baja cohesión y alto acoplamiento.');