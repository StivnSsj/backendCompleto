package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "programa_competencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramaCompetencia {
    @Id
    private String id; // VARCHAR(50) PRIMARY KEY

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "nivel", nullable = false, length = 50)
    private String nivel; // Ej: Básico, Intermedio, Avanzado
}