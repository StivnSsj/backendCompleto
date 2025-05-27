package co.edu.unicauca.api_rest.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer "; // Prefijo estándar

    public AuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
