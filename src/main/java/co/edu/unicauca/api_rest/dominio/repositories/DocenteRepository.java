package co.edu.unicauca.api_rest.dominio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unicauca.api_rest.dominio.model.Docente;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    // String porque el ID es String
    // Puedes añadir métodos personalizados aquí si los necesitas
}