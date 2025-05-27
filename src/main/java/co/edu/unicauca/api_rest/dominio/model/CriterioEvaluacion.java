package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "criterios_evaluacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriterioEvaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Un criterio pertenece a una rúbrica
    @JoinColumn(name = "rubrica_id", nullable = false)
    private Rubrica rubrica;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "ponderacion", nullable = false, precision = 5, scale = 2)
    private BigDecimal ponderacion; // DECIMAL(5,2)

    @OneToMany(mappedBy = "criterio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NivelDesempeno> niveles; // Lista de niveles de desempeño para este criterio
}