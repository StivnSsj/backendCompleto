package co.edu.unicauca.api_rest.application.controller;

import co.edu.unicauca.api_rest.application.dto.AsignacionDocenteAsignaturaDTO;
import co.edu.unicauca.api_rest.application.dto.AsignaturaDTO;
import co.edu.unicauca.api_rest.application.service.AsignacionDocenteAsignaturaService;
import co.edu.unicauca.api_rest.dominio.model.AsignaturaDocente;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaciones-docente-asignatura")
public class AsignacionDocenteAsignaturaController {

    private final AsignacionDocenteAsignaturaService asignacionService;

    @Autowired
    public AsignacionDocenteAsignaturaController(AsignacionDocenteAsignaturaService asignacionService) {
        this.asignacionService = asignacionService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Docente', 'Coordinador')")
    public ResponseEntity<AsignaturaDocente> asignarDocente(
            @Valid @RequestBody AsignacionDocenteAsignaturaDTO dto) {
        try {
            AsignaturaDocente nuevaAsignacion = asignacionService.asignarDocenteAAsignatura(
                    dto.getAsignaturaId(), dto.getDocenteId(), dto.getSemestreAcademico(), dto.getEsPrincipal());
            return new ResponseEntity<>(nuevaAsignacion, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/por-docente/{docenteId}/semestre/{semestreAcademico}")
    @PreAuthorize("hasAnyAuthority('Docente', 'Coordinador', 'Evaluador')")
    public ResponseEntity<List<AsignaturaDTO>> getAsignaturasDeDocente( // Cambia el tipo de retorno aquí
            @PathVariable Long docenteId,
            @PathVariable String semestreAcademico) {
        List<AsignaturaDTO> asignaturas = asignacionService.getAsignaturasByDocenteAndSemestre(docenteId, semestreAcademico);
        return new ResponseEntity<>(asignaturas, HttpStatus.OK);
    }
}