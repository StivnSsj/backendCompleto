package co.edu.unicauca.api_rest.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaDTO {
    private String id;
    private String nombre;
    private String descripcion;
    private Integer creditos;
    private Integer semestre;

    // Si estás usando un Mapeador (ej. MapStruct), puedes omitir un constructor
    // y solo tener los getters/setters o usar @Data de Lombok.
}
