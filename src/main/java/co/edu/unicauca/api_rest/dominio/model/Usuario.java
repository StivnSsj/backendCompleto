package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority; // Asegúrate de que esta entidad sea UserDetails
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Asegúrate de esta importación
import org.springframework.security.core.userdetails.UserDetails; // Implementar UserDetails

import java.util.Collection;
import java.util.Collections; // Para Collections.singletonList

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails { // Implementa UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O GenerationType.AUTO si H2 lo maneja
    private Long id; // El ID del usuario

    @Column(unique = true, nullable = false)
    private String correo; // Este es tu username para Spring Security

    @Column(nullable = false)
    private String password;

    private String nombre;
    private String apellido;

    @ManyToOne(fetch = FetchType.EAGER) // Eager para que el rol se cargue con el usuario
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(rol.getNombre()));
    }

    @Override
    public String getUsername() {
        return correo; // El correo es el username para Spring Security
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}