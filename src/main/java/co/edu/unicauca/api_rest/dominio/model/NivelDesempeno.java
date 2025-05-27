package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "niveles_desempeno")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NivelDesempeno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Un nivel de desempeño pertenece a un criterio
    @JoinColumn(name = "criterio_id", nullable = false)
    private CriterioEvaluacion criterio;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre; // Ej: Excelente, Bueno, Regular, Deficiente

    @Column(name = "puntuacion", nullable = false, precision = 5, scale = 2)
    private BigDecimal puntuacion; // Puntuación numérica para este nivel

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;
}