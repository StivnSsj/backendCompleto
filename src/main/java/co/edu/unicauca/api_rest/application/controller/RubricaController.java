package co.edu.unicauca.api_rest.application.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import co.edu.unicauca.api_rest.application.dto.RubricaCreateUpdateDTO;
import co.edu.unicauca.api_rest.application.dto.RubricaDTO;
import co.edu.unicauca.api_rest.application.service.RubricaService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/rubricas") // Base URL para este controlador
public class RubricaController {

    private final RubricaService rubricaService;

    @Autowired
    public RubricaController(RubricaService rubricaService) {
        this.rubricaService = rubricaService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Docente')")
    public ResponseEntity<RubricaDTO> createRubrica(@Valid @RequestBody RubricaCreateUpdateDTO dto) {
        RubricaDTO createdRubrica = rubricaService.createRubrica(dto);
        return new ResponseEntity<>(createdRubrica, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RubricaDTO> getRubricaById(@PathVariable Long id) {
        return rubricaService.getRubricaById(id)
                .map(rubrica -> new ResponseEntity<>(rubrica, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-asignatura-ra/{asignaturaRaId}")
    public ResponseEntity<List<RubricaDTO>> getRubricasByAsignaturaRA(@PathVariable Long asignaturaRaId) {
        List<RubricaDTO> rubricas = rubricaService.getRubricasByAsignaturaRA(asignaturaRaId);
        return new ResponseEntity<>(rubricas, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Docente')")
    public ResponseEntity<RubricaDTO> updateRubrica(@PathVariable Long id, @Valid @RequestBody RubricaCreateUpdateDTO dto) {
        try {
            RubricaDTO updatedRubrica = rubricaService.updateRubrica(id, dto);
            return new ResponseEntity<>(updatedRubrica, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Docente')")
    public ResponseEntity<Void> deleteRubrica(@PathVariable Long id) {
        try {
            rubricaService.deleteRubrica(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
