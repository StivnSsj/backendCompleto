package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "programa_ras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramaRA {
    @Id
    private String id; // VARCHAR(50) PRIMARY KEY

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @ManyToOne // Muchos RA de programa pueden estar asociados a una competencia
    @JoinColumn(name = "competencia_id", nullable = false)
    private ProgramaCompetencia competencia; // Relación con ProgramaCompetencia
}