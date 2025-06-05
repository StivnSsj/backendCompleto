package co.edu.unicauca.api_rest.dominio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unicauca.api_rest.application.dto.AsignaturaRADTO;
import co.edu.unicauca.api_rest.dominio.model.AsignaturaRA;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignaturaRARepository extends JpaRepository<AsignaturaRA, Long> {
    // Long porque el ID es Long (autogenerado)
    // Métodos personalizados (ejemplo)
    List<AsignaturaRA> findByAsignaturaIdAndDocenteId(String asignaturaId, Long docenteId);
    List<AsignaturaRA> findByDocenteId(Long docenteId);
    List<AsignaturaRA> findByAsignaturaId(String asignaturaId);
    Optional<AsignaturaRADTO> findByAsignaturaIdAndDocenteIdAndSemestreAcademico(String asignaturaId, Long docenteId,
            String semestreAcademico);
}
