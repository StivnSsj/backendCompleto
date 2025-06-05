package co.edu.unicauca.api_rest.dominio.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unicauca.api_rest.dominio.model.AsignaturaDocente;

@Repository
public interface AsignaturaDocenteRepository extends JpaRepository<AsignaturaDocente, Long> { // El tipo del ID es Long

    // Métodos de consulta útiles:
    List<AsignaturaDocente> findByDocenteId(Long docenteId);
    List<AsignaturaDocente> findByAsignaturaId(String asignaturaId);
    
    // Para encontrar una asignación específica por sus componentes principales:
    Optional<AsignaturaDocente> findByAsignaturaIdAndDocenteIdAndSemestreAcademico(String asignaturaId, Long docenteId, String semestreAcademico);
    List<AsignaturaDocente> findByDocenteIdAndSemestreAcademico(Long docenteId, String semestreAcademico);
    List<AsignaturaDocente> findByAsignaturaIdAndSemestreAcademico(String asignaturaId, String semestreAcademico);
}
