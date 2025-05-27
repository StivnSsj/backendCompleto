package co.edu.unicauca.api_rest.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionDTO {
    private Long id;
    private Long rubricaId;
    private String rubricaNombre;
    private Long asignaturaRaId; // Para referencia fácil
    private String asignaturaRaDescripcion;
    private String estudianteId;
    private String estudianteNombre; // Asumiendo que podemos obtener el nombre del estudiante
    private String evaluadorId;
    private String evaluadorNombre; // Nombre del docente o evaluador externo
    private LocalDateTime fechaEvaluacion;
    private BigDecimal puntuacionTotal;
    private String retroalimentacion;
    private List<EvaluacionDetalleDTO> detalles;
}
