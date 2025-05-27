package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estudiantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante {
    @Id
    @Column(name = "id_estudiante", nullable = false, length = 36)
    private String id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "codigo_estudiante", nullable = false, unique = true, length = 50)
    private String codigoEstudiante;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @Column(name = "programa_academico", length = 255)
    private String programaAcademico;
}
