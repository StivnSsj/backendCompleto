package co.edu.unicauca.api_rest.application.service;

import java.util.List;
import java.util.Optional;

import co.edu.unicauca.api_rest.application.dto.EvaluacionCreateDTO;
import co.edu.unicauca.api_rest.application.dto.EvaluacionDTO;
import co.edu.unicauca.api_rest.dominio.model.Evaluacion;

public interface EvaluacionService {
    EvaluacionDTO createEvaluacion(EvaluacionCreateDTO dto);
    Optional<EvaluacionDTO> getEvaluacionById(Long id);
    List<EvaluacionDTO> getEvaluacionesByEstudianteId(String estudianteId);
    List<EvaluacionDTO> getEvaluacionesByEvaluadorId(String evaluadorId);
    List<EvaluacionDTO> getEvaluacionesByRubricaId(Long rubricaId);
    void deleteEvaluacion(Long id);

    Evaluacion toEntity(EvaluacionCreateDTO dto);
    EvaluacionDTO toDto(Evaluacion entity);
}
