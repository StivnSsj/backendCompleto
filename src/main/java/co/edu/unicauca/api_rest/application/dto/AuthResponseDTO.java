package co.edu.unicauca.api_rest.application.dto;

import java.util.List;

public class AuthResponseDTO {
    private String accessToken;
    private List<String> roles; // Añade la lista de roles

    public AuthResponseDTO(String accessToken, List<String> roles) {
        this.accessToken = accessToken;
        this.roles = roles;
    }

    // Getters y Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
