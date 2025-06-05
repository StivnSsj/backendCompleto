package co.edu.unicauca.api_rest.application.service;

import co.edu.unicauca.api_rest.application.dto.AuthResponseDTO;
import co.edu.unicauca.api_rest.application.dto.RegisterRequest;
import co.edu.unicauca.api_rest.dominio.model.Docente;
import co.edu.unicauca.api_rest.dominio.model.Rol;
import co.edu.unicauca.api_rest.dominio.model.Usuario;
import co.edu.unicauca.api_rest.dominio.repositories.DocenteRepository;
import co.edu.unicauca.api_rest.dominio.repositories.RolRepository;
import co.edu.unicauca.api_rest.dominio.repositories.UsuarioRepository;
import co.edu.unicauca.api_rest.security.JwtGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final DocenteRepository docenteRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       RolRepository rolRepository,
                       PasswordEncoder passwordEncoder,
                       JwtGenerator jwtGenerator,
                       DocenteRepository docenteRepository) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.docenteRepository = docenteRepository;
    }

    public AuthResponseDTO login(String correo, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(correo, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new AuthResponseDTO(token);
    }

    @Transactional
    public AuthResponseDTO register(RegisterRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está registrado.");
        }

        // 1. Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol especificado no existe."));
        usuario.setRol(rol);

        Usuario savedUsuario = usuarioRepository.save(usuario); // Guarda el usuario para obtener su ID autogenerado

        // 2. Lógica condicional para Docente
        if ("ROL_DOCENTE".equals(request.getRolId())) {
            // Asegúrate de que los datos específicos del docente no sean nulos
            if (request.getTipoIdentificacion() == null || request.getIdentificacion() == null ||
                request.getTipoDocente() == null || request.getUltimoTitulo() == null) {
                // Considera un mensaje de error más específico para faltantes de datos del docente
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltan datos específicos del docente para el rol ROL_DOCENTE.");
            }

            Docente docente = new Docente();
            // ¡ELIMINADO! NO establecer el ID manualmente cuando se usa @MapsId
            // docente.setId(savedUsuario.getId()); // <--- ¡QUITA ESTA LÍNEA!

            // Establece la relación con el usuario guardado. Hibernate derivará el ID desde aquí.
            docente.setUsuario(savedUsuario);

            docente.setNombres(request.getNombre());
            docente.setApellidos(request.getApellido());
            docente.setTipoIdentificacion(request.getTipoIdentificacion());
            docente.setIdentificacion(request.getIdentificacion());
            docente.setTipoDocente(request.getTipoDocente());
            docente.setCorreoInstitucional(request.getCorreo());
            docente.setUltimoTitulo(request.getUltimoTitulo());

            docenteRepository.save(docente);
        }

        // Después de registrar y guardar el usuario (y docente si aplica), generas el token
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            savedUsuario.getCorreo(),
            null, // La contraseña no es necesaria para crear el Authentication token para generar JWT
            savedUsuario.getAuthorities() // Asegúrate de que tu entidad Usuario implemente UserDetails y sobrescriba getAuthorities()
        );
        // Opcional: Si quieres que este usuario esté autenticado en el contexto de la sesión actual
        // SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication); // Genera el token con los roles incluidos

        return new AuthResponseDTO(token);
    }
}