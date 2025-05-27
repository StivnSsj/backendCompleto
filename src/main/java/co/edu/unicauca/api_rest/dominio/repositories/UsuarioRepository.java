package co.edu.unicauca.api_rest.dominio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unicauca.api_rest.dominio.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    // String porque el ID es String
    // Puedes añadir métodos personalizados aquí si los necesitas
    Usuario findByCorreo(String correo); // Ejemplo: Buscar usuario por correo
}
