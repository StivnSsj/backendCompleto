package co.edu.unicauca.api_rest.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NivelDesempenoDTO {
    private Long id; // Puede ser nulo para creación
    @NotBlank(message = "El nombre del nivel no puede estar vacío")
    @Size(max = 100, message = "El nombre del nivel no puede exceder los 100 caracteres")
    private String nombre; // Ej: Excelente, Bueno

    @NotNull(message = "La puntuación del nivel no puede ser nula")
    @DecimalMin(value = "0.0", message = "La puntuación debe ser un valor positivo")
    private BigDecimal puntuacion;

    @NotBlank(message = "La descripción del nivel no puede estar vacía")
    @Size(max = 1000, message = "La descripción del nivel no puede exceder los 1000 caracteres")
    private String descripcion;
}
