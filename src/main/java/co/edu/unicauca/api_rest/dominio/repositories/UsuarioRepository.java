package co.edu.unicauca.api_rest.dominio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unicauca.api_rest.dominio.model.Usuario;

import java.util.Optional; // Importa Optional

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método para buscar un usuario por su correo.
    // Spring Data JPA lo implementa automáticamente basado en el nombre del método.
    Optional<Usuario> findByCorreo(String correo);

    // Método para verificar si un usuario existe por su correo.
    // También implementado automáticamente por Spring Data JPA.
    Boolean existsByCorreo(String correo);

    // Si necesitas buscar por ID de usuario (que ahora es Long):
    // Optional<Usuario> findById(Long id); // Ya heredado de JpaRepository
}
