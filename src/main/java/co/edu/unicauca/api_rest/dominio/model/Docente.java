package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "docentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Docente {
    @Id
    private String id; // VARCHAR(50) PRIMARY KEY

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "tipo_identificacion", length = 50)
    private String tipoIdentificacion;

    @Column(name = "identificacion", unique = true, nullable = false, length = 50)
    private String identificacion;

    @Column(name = "tipo_docente", length = 50)
    private String tipoDocente; // Ej: Cátedra, Tiempo Completo, Planta

    @Column(name = "correo_institucional", unique = true, nullable = false, length = 255)
    private String correoInstitucional;

    @Column(name = "ultimo_titulo", length = 255)
    private String ultimoTitulo;
}