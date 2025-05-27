package co.edu.unicauca.api_rest.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaRADTO {
    private Long id;
    private String asignaturaId;
    private String asignaturaNombre; // Podríamos incluir el nombre para el front
    private String docenteId;
    private String docenteNombre; // Podríamos incluir el nombre para el front
    private String descripcion;
    private String semestreAcademico;
    private String programaRaId;
    private String programaRaDescripcion; // Descripción del RAP general
}