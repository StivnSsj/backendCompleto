package co.edu.unicauca.api_rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User; // Importa la clase User de Spring Security
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import co.edu.unicauca.api_rest.dominio.model.Usuario;
import co.edu.unicauca.api_rest.dominio.repositories.UsuarioRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List; // Necesario si manejas más de un rol por usuario

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // Corrección 1: Manejar el Optional devuelto por findByCorreo
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        // Corrección 2 (Verificación): Asegúrate de que tu entidad Usuario
        // tenga un método getRol() que devuelva un objeto Rol,
        // y que Rol tenga un método getNombre() que devuelva el String del rol (ej. "ROL_DOCENTE").
        // También verifica que tu entidad Usuario implemente UserDetails y sobrescriba getAuthorities()
        // como te mostré anteriormente.
        // Si tu entidad Usuario implementa UserDetails, puedes simplemente retornar la instancia de Usuario:
        return usuario; // ¡Esta es la forma más limpia si Usuario implementa UserDetails!

        // Si tu entidad Usuario NO implementa UserDetails, entonces usa la siguiente línea:
        // return new User(usuario.getCorreo(), usuario.getPassword(), mapRolesToAuthorities(usuario.getRol().getNombre()));
    }

    // Si tu entidad Usuario ya implementa UserDetails (como te sugerí antes),
    // este método mapRolesToAuthorities() podría no ser necesario aquí directamente,
    // ya que Usuario.getAuthorities() lo haría.
    // Sin embargo, si decides no implementar UserDetails en Usuario, este método sería útil.
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(String roleName) {
        // Dado que usas hasAnyAuthority('ROL_DOCENTE'), el nombre del rol DEBE coincidir
        // exactamente con el String que tienes en tu base de datos para el rol (ej. "ROL_DOCENTE").
        // No necesitas prefijar con "ROLE_" si tus anotaciones usan hasAnyAuthority().
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }
}