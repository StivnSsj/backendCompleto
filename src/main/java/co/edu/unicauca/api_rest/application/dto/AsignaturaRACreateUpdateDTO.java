package co.edu.unicauca.api_rest.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaRACreateUpdateDTO {
    @NotBlank(message = "El ID de la asignatura no puede estar vacío")
    private String asignaturaId; // ID de la asignatura

    @NotBlank(message = "El ID del docente no puede estar vacío")
    private Long docenteId;    // ID del docente

    @NotBlank(message = "La descripción del RA no puede estar vacía")
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;

    @NotBlank(message = "El semestre académico no puede estar vacío")
    private String semestreAcademico;

    private String programaRaId; // Opcional, ID del RAP general al que se vincula
}