package co.edu.unicauca.api_rest.dominio.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asignatura_docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class AsignaturaDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy es bueno aquí
    @JoinColumn(name = "asignatura_id", nullable = false)
    @JsonIgnoreProperties({"asignaturasDocentes"}) // Ignorar la colección en Asignatura al serializar AsignaturaDocente
    private Asignatura asignatura;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy es bueno aquí
    @JoinColumn(name = "docente_id", nullable = false)
    @JsonIgnoreProperties({"asignaturasDocentes"}) // Ignorar la colección en Docente al serializar AsignaturaDocente
    private Docente docente;

    @Column(name = "semestre_academico", nullable = false, length = 10)
    private String semestreAcademico;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false;

    // Constructor para facilitar la creación de nuevas asignaciones
    public AsignaturaDocente(Asignatura asignatura, Docente docente, String semestreAcademico, Boolean esPrincipal) {
        this.asignatura = asignatura;
        this.docente = docente;
        this.semestreAcademico = semestreAcademico;
        this.esPrincipal = esPrincipal;
    }
}
