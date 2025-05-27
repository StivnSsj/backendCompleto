package co.edu.unicauca.api_rest.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RubricaDTO {
    private Long id;
    private String nombre;
    private Long asignaturaRaId;
    private String asignaturaRaDescripcion; // Para dar más contexto
    private List<CriterioEvaluacionDTO> criterios;
}
