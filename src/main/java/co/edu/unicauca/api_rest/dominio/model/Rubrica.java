package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "rubricas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rubrica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @ManyToOne // Una rúbrica se asocia a un RA de una asignatura
    @JoinColumn(name = "asignatura_ra_id", nullable = false)
    private AsignaturaRA asignaturaRa;

    @OneToMany(mappedBy = "rubrica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CriterioEvaluacion> criterios; // Lista de criterios de evaluación de esta rúbrica
}