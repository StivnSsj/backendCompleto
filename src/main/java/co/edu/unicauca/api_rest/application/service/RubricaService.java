package co.edu.unicauca.api_rest.application.service;

import java.util.List;
import java.util.Optional;

import co.edu.unicauca.api_rest.application.dto.RubricaCreateUpdateDTO;
import co.edu.unicauca.api_rest.application.dto.RubricaDTO;
import co.edu.unicauca.api_rest.dominio.model.Rubrica;

public interface RubricaService {
    RubricaDTO createRubrica(RubricaCreateUpdateDTO dto);
    RubricaDTO updateRubrica(Long id, RubricaCreateUpdateDTO dto);
    Optional<RubricaDTO> getRubricaById(Long id);
    List<RubricaDTO> getRubricasByAsignaturaRA(Long asignaturaRaId);
    void deleteRubrica(Long id);

    Rubrica toEntity(RubricaCreateUpdateDTO dto);
    RubricaDTO toDto(Rubrica entity);
}