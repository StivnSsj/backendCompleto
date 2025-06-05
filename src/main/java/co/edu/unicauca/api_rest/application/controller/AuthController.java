package co.edu.unicauca.api_rest.application.controller;

import co.edu.unicauca.api_rest.application.dto.AuthResponseDTO;
import co.edu.unicauca.api_rest.application.dto.LoginDTO;
import co.edu.unicauca.api_rest.application.dto.RegisterRequest; // Importar
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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginRequest) {
        AuthResponseDTO response = authService.login(loginRequest.getCorreo(), loginRequest.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponseDTO response = authService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}