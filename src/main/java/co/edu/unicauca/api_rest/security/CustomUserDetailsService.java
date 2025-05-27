package co.edu.unicauca.api_rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unicauca.api_rest.dominio.model.Usuario;
import co.edu.unicauca.api_rest.dominio.repositories.UsuarioRepository;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con correo: " + correo);
        }
        return new User(usuario.getCorreo(), usuario.getPassword(), mapRolesToAuthorities(usuario.getRol().getNombre()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(String roleName) {
        // Spring Security espera los roles con el prefijo "ROLE_" por defecto
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }
}
