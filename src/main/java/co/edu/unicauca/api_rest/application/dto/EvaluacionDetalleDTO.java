package co.edu.unicauca.api_rest.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionDetalleDTO {
    private Long id; // Puede ser nulo para creación
    @NotNull(message = "El ID del criterio no puede ser nulo")
    private Long criterioId;

    @NotNull(message = "El ID del nivel seleccionado no puede ser nulo")
    private Long nivelSeleccionadoId;
}
