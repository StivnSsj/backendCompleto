package co.edu.unicauca.api_rest.application.controller;

import co.edu.unicauca.api_rest.application.dto.AuthResponseDTO;
import co.edu.unicauca.api_rest.application.dto.LoginDTO;
import co.edu.unicauca.api_rest.application.dto.RegisterRequest; // Asegúrate de que el import sea correcto
import co.edu.unicauca.api_rest.application.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Inyección de dependencia por constructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para el inicio de sesión de usuarios.
     * Delega la lógica de autenticación y generación de token/roles al AuthService.
     * @param loginRequest DTO con las credenciales del usuario.
     * @return ResponseEntity con AuthResponse (token y roles) y HttpStatus.OK.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginRequest) {
        AuthResponseDTO response = authService.login(loginRequest.getCorreo(), loginRequest.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint para el registro de nuevos usuarios.
     * Delega la lógica de creación de usuario/docente y generación de token/roles al AuthService.
     * @param registerRequest DTO con los datos del nuevo usuario y, opcionalmente, datos del docente.
     * @return ResponseEntity con AuthResponse (token y roles) y HttpStatus.CREATED.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponseDTO response = authService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}