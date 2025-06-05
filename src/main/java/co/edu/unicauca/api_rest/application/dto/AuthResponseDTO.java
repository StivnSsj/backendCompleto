package co.edu.unicauca.api_rest.application.dto;

public class AuthResponseDTO {
    private String token; // El token JWT generado

    // Constructor
    public AuthResponseDTO(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }

    // Setter (opcional, pero buena práctica si se necesita en algún contexto de mapeo)
    public void setToken(String token) {
        this.token = token;
    }
}
