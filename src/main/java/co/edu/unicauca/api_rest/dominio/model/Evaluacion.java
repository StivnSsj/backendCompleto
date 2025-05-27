package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "evaluaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Una evaluación usa una rúbrica
    @JoinColumn(name = "rubrica_id", nullable = false)
    private Rubrica rubrica;

    @Column(name = "estudiante_id", nullable = false, length = 50)
    private String estudianteId; // Asumimos String, podría ser objeto Estudiante

    @Column(name = "evaluador_id", nullable = false, length = 50)
    private String evaluadorId; // ID del docente o evaluador externo

    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDateTime fechaEvaluacion;

    @Column(name = "puntuacion_total", nullable = false, precision = 5, scale = 2)
    private BigDecimal puntuacionTotal;

    @Column(name = "retroalimentacion", columnDefinition = "TEXT")
    private String retroalimentacion;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluacionDetalle> detalles; // Detalle de la selección por criterio
}