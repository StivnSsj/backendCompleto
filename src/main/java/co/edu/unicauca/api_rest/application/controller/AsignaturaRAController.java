package co.edu.unicauca.api_rest.application.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import co.edu.unicauca.api_rest.application.dto.AsignaturaRACreateUpdateDTO;
import co.edu.unicauca.api_rest.application.dto.AsignaturaRADTO;
import co.edu.unicauca.api_rest.application.service.AsignaturaRAService;
import jakarta.persistence.EntityNotFoundException; // Importar para un manejo de errores más específico

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas-ra") // Base URL para este controlador
public class AsignaturaRAController {

    private final AsignaturaRAService asignaturaRAService;

    @Autowired
    public AsignaturaRAController(AsignaturaRAService asignaturaRAService) {
        this.asignaturaRAService = asignaturaRAService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Docente', 'Coordinador')") // Solo docentes y coordinadores pueden crear
    public ResponseEntity<AsignaturaRADTO> createAsignaturaRA(@Valid @RequestBody AsignaturaRACreateUpdateDTO dto) {
        AsignaturaRADTO savedRA = asignaturaRAService.saveAsignaturaRA(dto);
        return new ResponseEntity<>(savedRA, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaRADTO> getAsignaturaRAById(@PathVariable Long id) {
        return asignaturaRAService.getAsignaturaRADTOById(id)
                .map(ra -> new ResponseEntity<>(ra, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-asignatura-docente")
    public ResponseEntity<List<AsignaturaRADTO>> getAsignaturaRAsByAsignaturaAndDocente(
            @RequestParam String asignaturaId,
            @RequestParam String docenteId) {
        List<AsignaturaRADTO> ras = asignaturaRAService.getAsignaturaRAsByAsignaturaAndDocente(asignaturaId, docenteId);
        return new ResponseEntity<>(ras, HttpStatus.OK);
    }

    @GetMapping("/by-docente/{docenteId}")
    public ResponseEntity<List<AsignaturaRADTO>> getAsignaturaRAsByDocente(@PathVariable String docenteId) {
        List<AsignaturaRADTO> ras = asignaturaRAService.getAsignaturaRAsByDocente(docenteId);
        return new ResponseEntity<>(ras, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Docente', 'Coordinador')")
    public ResponseEntity<AsignaturaRADTO> updateAsignaturaRA(@PathVariable Long id, @Valid @RequestBody AsignaturaRACreateUpdateDTO dto) {
        try {
            AsignaturaRADTO updatedRA = asignaturaRAService.updateAsignaturaRA(id, dto);
            return new ResponseEntity<>(updatedRA, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Docente', 'Coordinador')")
    public ResponseEntity<Void> deleteAsignaturaRA(@PathVariable Long id) {
        try {
            asignaturaRAService.deleteAsignaturaRA(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/copy-from-semester")
    @PreAuthorize("hasAnyAuthority('Docente')")
    public ResponseEntity<List<AsignaturaRADTO>> copyAsignaturaRAs(
            @RequestParam String asignaturaId,
            @RequestParam String docenteId,
            @RequestParam String previousSemester) {
        List<AsignaturaRADTO> copiedRAs = asignaturaRAService.copyAsignaturaRAsFromPreviousSemester(asignaturaId, docenteId, previousSemester);
        return new ResponseEntity<>(copiedRAs, HttpStatus.CREATED);
    }
}
