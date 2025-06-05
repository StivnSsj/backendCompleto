package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asignatura_docente")
@Data
@NoArgsConstructor
public class AsignaturaDocente {

    // Si tu PK es auto-generada (como en el SQL que te di):
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación Many-to-One con Asignatura
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignatura_id", nullable = false)
    private Asignatura asignatura;

    // Relación Many-to-One con Docente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @Column(name = "semestre_academico", nullable = false, length = 10)
    private String semestreAcademico;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false; // Valor por defecto

    // Constructor para facilitar la creación de nuevas asignaciones
    public AsignaturaDocente(Asignatura asignatura, Docente docente, String semestreAcademico, Boolean esPrincipal) {
        this.asignatura = asignatura;
        this.docente = docente;
        this.semestreAcademico = semestreAcademico;
        this.esPrincipal = esPrincipal;
    }
}
