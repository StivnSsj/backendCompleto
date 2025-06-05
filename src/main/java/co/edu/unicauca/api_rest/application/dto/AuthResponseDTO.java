package co.edu.unicauca.api_rest.application.dto;

import java.util.List; // Importar List

public class AuthResponseDTO {
    private String token;
    private List<String> roles; // ¡Campo añadido!

    // Constructor actualizado
    public AuthResponseDTO(String token, List<String> roles) {
        this.token = token;
        this.roles = roles;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public List<String> getRoles() { // Nuevo getter
        return roles;
    }

    // Setters (opcional, pero buena práctica si se necesita en algún contexto de mapeo)
    public void setToken(String token) {
        this.token = token;
    }

    public void setRoles(List<String> roles) { // Nuevo setter
        this.roles = roles;
    }
}
