package co.edu.unicauca.api_rest.dominio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unicauca.api_rest.dominio.model.ProgramaCompetencia;

@Repository
public interface ProgramaCompetenciaRepository extends JpaRepository<ProgramaCompetencia, String> {
    // String porque el ID es String
    // Puedes añadir métodos personalizados aquí si los necesitas
}