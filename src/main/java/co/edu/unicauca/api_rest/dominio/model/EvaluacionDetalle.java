package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evaluacion_detalles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Un detalle pertenece a una evaluación
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    @ManyToOne // Un detalle registra la evaluación de un criterio específico
    @JoinColumn(name = "criterio_id", nullable = false)
    private CriterioEvaluacion criterio;

    @ManyToOne // Un detalle registra el nivel de desempeño seleccionado para ese criterio
    @JoinColumn(name = "nivel_seleccionado_id", nullable = false)
    private NivelDesempeno nivelSeleccionado;
}