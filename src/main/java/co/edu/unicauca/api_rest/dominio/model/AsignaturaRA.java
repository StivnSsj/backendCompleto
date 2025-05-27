package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "asignatura_ras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaRA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Una asignatura_RA pertenece a una Asignatura
    @JoinColumn(name = "asignatura_id", nullable = false)
    private Asignatura asignatura; // Relación con Asignatura

    @ManyToOne // Una asignatura_RA es gestionada por un Docente
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente; // Relación con Docente

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "semestre_academico", nullable = false, length = 50)
    private String semestreAcademico;

    @ManyToOne // Opcional: Relación con ProgramaRA para vincular
    @JoinColumn(name = "programa_ra_id")
    private ProgramaRA programaRa; // Relación con ProgramaRA (puede ser nulo)
}