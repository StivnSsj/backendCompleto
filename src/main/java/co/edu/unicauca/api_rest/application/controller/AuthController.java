package co.edu.unicauca.api_rest.application.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder; // Importar PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException; // Para manejar excepciones

import co.edu.unicauca.api_rest.application.dto.AuthResponseDTO;
import co.edu.unicauca.api_rest.application.dto.LoginDTO;
import co.edu.unicauca.api_rest.application.dto.RegisterDTO;
import co.edu.unicauca.api_rest.dominio.model.Rol;
import co.edu.unicauca.api_rest.dominio.model.Usuario;
import co.edu.unicauca.api_rest.dominio.repositories.RolRepository;
import co.edu.unicauca.api_rest.dominio.repositories.UsuarioRepository;
import co.edu.unicauca.api_rest.security.JwtGenerator;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final UsuarioRepository usuarioRepository; // Inyectar UsuarioRepository
    private final RolRepository rolRepository;         // Inyectar RolRepository
    private final PasswordEncoder passwordEncoder;     // Inyectar PasswordEncoder

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtGenerator jwtGenerator,
                          UsuarioRepository usuarioRepository, RolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Endpoint para el inicio de sesión de usuarios.
     * @param loginDTO Credenciales de usuario (correo y contraseña).
     * @return Token JWT si la autenticación es exitosa.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getCorreo(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }

    /**
     * Endpoint para el registro de nuevos usuarios.
     * Rol: COORDINADOR (o cualquier rol que tenga permiso para crear usuarios)
     * Puede ser necesario que solo el Coordinador pueda registrar nuevos usuarios.
     * Para este ejemplo, no se requiere autenticación para registrar (es público).
     * Si quieres que solo un COORDINADOR pueda registrar, necesitarías aplicar
     * @PreAuthorize("hasAuthority('ROL_COORDINADOR')") y que este endpoint
     * esté protegido, lo cual implicaría que el primer coordinador se cree
     * vía `data.sql` o que haya un endpoint de registro inicial sin protección.
     * Por simplicidad, lo dejaremos accesible por ahora, pero tenlo en cuenta para producción.
     * @param registerDTO Datos del nuevo usuario a registrar.
     * @return El usuario registrado con un mensaje de éxito.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
        if (usuarioRepository.existsById(registerDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de usuario ya existe.");
        }
        if (usuarioRepository.findByCorreo(registerDTO.getCorreo()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setId(registerDTO.getId());
        usuario.setNombre(registerDTO.getNombre());
        usuario.setApellido(registerDTO.getApellido());
        usuario.setCorreo(registerDTO.getCorreo());
        usuario.setPassword(passwordEncoder.encode(registerDTO.getPassword())); // Codificar la contraseña

        Rol rol = rolRepository.findById(registerDTO.getRolId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol especificado no existe."));
        usuario.setRol(rol);

        usuarioRepository.save(usuario);

        return new ResponseEntity<>("Usuario registrado exitosamente!", HttpStatus.CREATED);
    }
}