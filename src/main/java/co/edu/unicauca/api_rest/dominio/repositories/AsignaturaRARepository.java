package co.edu.unicauca.api_rest.dominio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unicauca.api_rest.dominio.model.AsignaturaRA;

import java.util.List;

@Repository
public interface AsignaturaRARepository extends JpaRepository<AsignaturaRA, Long> {
    // Long porque el ID es Long (autogenerado)
    // Métodos personalizados (ejemplo)
    List<AsignaturaRA> findByAsignaturaIdAndDocenteId(String asignaturaId, String docenteId);
    List<AsignaturaRA> findByDocenteId(String docenteId);
    List<AsignaturaRA> findByAsignaturaId(String asignaturaId);
}
