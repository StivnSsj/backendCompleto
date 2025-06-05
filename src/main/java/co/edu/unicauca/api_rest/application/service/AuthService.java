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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

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

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new AuthResponseDTO(token, roles);
    }

    @Transactional
    public AuthResponseDTO register(RegisterRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol especificado no existe."));
        usuario.setRol(rol);

        Usuario savedUsuario = usuarioRepository.save(usuario);

        if ("ROL_DOCENTE".equals(request.getRolId())) {
            if (request.getTipoIdentificacion() == null || request.getIdentificacion() == null ||
                request.getTipoDocente() == null || request.getUltimoTitulo() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltan datos específicos del docente para el rol ROL_DOCENTE.");
            }

            Docente docente = new Docente();
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

        // Obtener los roles del usuario recién guardado
        List<String> roles = savedUsuario.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList());

        // Crear un objeto Authentication para el usuario recién registrado
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
            savedUsuario.getCorreo(),
            null, // No necesitas la contraseña aquí para crear el token
            savedUsuario.getAuthorities()
        );

        // Generar el token usando el 'newAuth' creado
        String tokenForNewUser = jwtGenerator.generateToken(newAuth); // <-- ¡CORREGIDO AQUÍ!

        return new AuthResponseDTO(tokenForNewUser, roles);
    }
}