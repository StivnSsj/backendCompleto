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
public class RubricaCreateUpdateDTO {
    @NotBlank(message = "El nombre de la rúbrica no puede estar vacío")
    @Size(max = 255, message = "El nombre de la rúbrica no puede exceder los 255 caracteres")
    private String nombre;

    @NotNull(message = "El ID del RA de asignatura no puede ser nulo")
    private Long asignaturaRaId; // ID del AsignaturaRA al que se asocia la rúbrica

    @NotNull(message = "La rúbrica debe tener al menos un criterio de evaluación")
    @Size(min = 1, message = "La rúbrica debe tener al menos un criterio de evaluación")
    @Valid // Para validar los DTOs anidados
    private List<CriterioEvaluacionDTO> criterios;
}