package co.edu.unicauca.api_rest.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error; // Ej: "Not Found", "Bad Request"
    private String message; // Mensaje descriptivo del error
    private String path;    // La ruta de la solicitud que causó el error
}
