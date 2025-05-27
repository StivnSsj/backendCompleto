package co.edu.unicauca.api_rest.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriterioEvaluacionDTO {
    private Long id; // Puede ser nulo para creación
    @NotBlank(message = "La descripción del criterio no puede estar vacía")
    @Size(max = 500, message = "La descripción del criterio no puede exceder los 500 caracteres")
    private String descripcion;

    @NotNull(message = "La ponderación del criterio no puede ser nula")
    @DecimalMin(value = "0.0", message = "La ponderación debe ser un valor positivo")
    @DecimalMax(value = "1.0", message = "La ponderación no debe exceder 1.0 (100%)")
    private BigDecimal ponderacion; // Representa un porcentaje (ej: 0.20 para 20%)

    @NotNull(message = "Un criterio debe tener al menos un nivel de desempeño")
    @Size(min = 1, message = "Un criterio debe tener al menos un nivel de desempeño")
    @Valid // Para validar los DTOs anidados
    private List<NivelDesempenoDTO> niveles;
}