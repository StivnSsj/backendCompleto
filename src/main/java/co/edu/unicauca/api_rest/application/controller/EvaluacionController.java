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
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    @Autowired
    public EvaluacionController(EvaluacionService evaluacionService) {
        this.evaluacionService = evaluacionService;
    }

    
    @PostMapping
    @PreAuthorize("hasAnyAuthority('Docente', 'Evaluador Externo')")
    public ResponseEntity<EvaluacionDTO> createEvaluacion(@Valid @RequestBody EvaluacionCreateDTO dto) {
        EvaluacionDTO createdEvaluacion = evaluacionService.createEvaluacion(dto);
        return new ResponseEntity<>(createdEvaluacion, HttpStatus.CREATED);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Docente', 'Evaluador Externo')")
    public ResponseEntity<EvaluacionDTO> getEvaluacionById(@PathVariable Long id) {
        return evaluacionService.getEvaluacionById(id)
                .map(evaluacion -> new ResponseEntity<>(evaluacion, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    
    @GetMapping("/by-estudiante/{estudianteId}")
    @PreAuthorize("hasAnyAuthority('Docente', 'Evaluador Externo')")
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesByEstudianteId(@PathVariable String estudianteId) {
        List<EvaluacionDTO> evaluaciones = evaluacionService.getEvaluacionesByEstudianteId(estudianteId);
        return new ResponseEntity<>(evaluaciones, HttpStatus.OK);
    }

   
    @GetMapping("/by-evaluador/{evaluadorId}")
    @PreAuthorize("hasAnyAuthority('Docente', 'Evaluador Externo')")
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesByEvaluadorId(@PathVariable String evaluadorId) {
        List<EvaluacionDTO> evaluaciones = evaluacionService.getEvaluacionesByEvaluadorId(evaluadorId);
        return new ResponseEntity<>(evaluaciones, HttpStatus.OK);
    }

   
    @GetMapping("/by-rubrica/{rubricaId}")
    @PreAuthorize("hasAnyAuthority('Docente', 'Evaluador Externo')")
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesByRubricaId(@PathVariable Long rubricaId) {
        List<EvaluacionDTO> evaluaciones = evaluacionService.getEvaluacionesByRubricaId(rubricaId);
        return new ResponseEntity<>(evaluaciones, HttpStatus.OK);
    }

    
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
