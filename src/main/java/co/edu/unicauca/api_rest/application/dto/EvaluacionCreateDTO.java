package co.edu.unicauca.api_rest.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionCreateDTO {
    @NotNull(message = "El ID de la rúbrica no puede ser nulo")
    private Long rubricaId;

    @NotBlank(message = "El ID del estudiante no puede estar vacío")
    private String estudianteId;

    @NotBlank(message = "El ID del evaluador no puede estar vacío")
    private String evaluadorId; // Puede ser Docente o Evaluador Externo

    @Size(max = 1000, message = "La retroalimentación no puede exceder los 1000 caracteres")
    private String retroalimentacion; // Opcional

    @NotNull(message = "Los detalles de la evaluación no pueden ser nulos")
    @Size(min = 1, message = "Debe haber al menos un detalle de evaluación")
    @Valid // Para validar los DTOs anidados
    private List<EvaluacionDetalleDTO> detalles;
}