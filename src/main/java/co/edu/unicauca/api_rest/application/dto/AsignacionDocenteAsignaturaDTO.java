package co.edu.unicauca.api_rest.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionDocenteAsignaturaDTO {
    @NotBlank
    private String asignaturaId;
    @NotNull
    private Long docenteId;
    @NotBlank
    private String semestreAcademico;
    @NotNull
    private Boolean esPrincipal;
}