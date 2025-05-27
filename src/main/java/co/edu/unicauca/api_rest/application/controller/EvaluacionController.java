package co.edu.unicauca.api_rest.application.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import co.edu.unicauca.api_rest.application.dto.EvaluacionCreateDTO;
import co.edu.unicauca.api_rest.application.dto.EvaluacionDTO;
import co.edu.unicauca.api_rest.application.service.EvaluacionService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones") // Base URL para este controlador
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    @Autowired
    public EvaluacionController(EvaluacionService evaluacionService) {
        this.evaluacionService = evaluacionService;
    }

    /**
     * Permite a un Docente o Evaluador Externo registrar una nueva evaluación de un estudiante.
     * Rol: Docente, Evaluador Externo
     * CU-EVAL-001: Asignar Puntuación a Estudiante (Rúbrica)
     * @param dto Datos de la evaluación a crear.
     * @return La evaluación creada con su ID y puntuación total.
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('Docente', 'Evaluador Externo')")
    public ResponseEntity<EvaluacionDTO> createEvaluacion(@Valid @RequestBody EvaluacionCreateDTO dto) {
        EvaluacionDTO createdEvaluacion = evaluacionService.createEvaluacion(dto);
        return new ResponseEntity<>(createdEvaluacion, HttpStatus.CREATED);
    }

    /**
     * Obtiene una evaluación por su ID.
     * Rol: Docente, Evaluador Externo
     * CU-EVAL-002: Visualizar Evaluaciones de Estudiantes
     * @param id ID de la evaluación.
     * @return La evaluación.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionDTO> getEvaluacionById(@PathVariable Long id) {
        return evaluacionService.getEvaluacionById(id)
                .map(evaluacion -> new ResponseEntity<>(evaluacion, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Obtiene todas las evaluaciones realizadas para un estudiante específico.
     * Rol: Docente, Evaluador Externo
     * CU-EVAL-002: Visualizar Evaluaciones de Estudiantes
     * @param estudianteId ID del estudiante.
     * @return Lista de evaluaciones del estudiante.
     */
    @GetMapping("/by-estudiante/{estudianteId}")
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesByEstudianteId(@PathVariable String estudianteId) {
        List<EvaluacionDTO> evaluaciones = evaluacionService.getEvaluacionesByEstudianteId(estudianteId);
        return new ResponseEntity<>(evaluaciones, HttpStatus.OK);
    }

    /**
     * Obtiene todas las evaluaciones realizadas por un evaluador específico.
     * Rol: Docente, Evaluador Externo
     * CU-EVAL-002: Visualizar Evaluaciones de Estudiantes
     * @param evaluadorId ID del evaluador (Docente o Evaluador Externo).
     * @return Lista de evaluaciones realizadas por el evaluador.
     */
    @GetMapping("/by-evaluador/{evaluadorId}")
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesByEvaluadorId(@PathVariable String evaluadorId) {
        List<EvaluacionDTO> evaluaciones = evaluacionService.getEvaluacionesByEvaluadorId(evaluadorId);
        return new ResponseEntity<>(evaluaciones, HttpStatus.OK);
    }

    /**
     * Obtiene todas las evaluaciones que han utilizado una rúbrica específica.
     * Rol: Docente, Evaluador Externo
     * @param rubricaId ID de la rúbrica.
     * @return Lista de evaluaciones que usaron la rúbrica.
     */
    @GetMapping("/by-rubrica/{rubricaId}")
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesByRubricaId(@PathVariable Long rubricaId) {
        List<EvaluacionDTO> evaluaciones = evaluacionService.getEvaluacionesByRubricaId(rubricaId);
        return new ResponseEntity<>(evaluaciones, HttpStatus.OK);
    }

    /**
     * Elimina una evaluación.
     * Rol: Docente (o Coordinador, con lógica de seguridad adicional)
     * @param id ID de la evaluación a eliminar.
     * @return Respuesta sin contenido si la eliminación fue exitosa.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Docente', 'Evaluador Externo')")
    public ResponseEntity<Void> deleteEvaluacion(@PathVariable Long id) {
        try {
            evaluacionService.deleteEvaluacion(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
