package co.edu.unicauca.api_rest.dominio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unicauca.api_rest.dominio.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, String> {
    // String porque el ID es String
    // Puedes añadir métodos personalizados aquí si los necesitas
}
