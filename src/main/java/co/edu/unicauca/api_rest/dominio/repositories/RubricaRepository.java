package co.edu.unicauca.api_rest.dominio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unicauca.api_rest.dominio.model.Rubrica;

@Repository
public interface RubricaRepository extends JpaRepository<Rubrica, Long> {
    // Long porque el ID es Long (autogenerado)
    // Puedes añadir métodos personalizados aquí si los necesitas
}
