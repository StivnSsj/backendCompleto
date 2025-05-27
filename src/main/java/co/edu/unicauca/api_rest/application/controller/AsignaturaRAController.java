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

    /**
     * Permite a un Docente o Coordinador crear un nuevo Resultado de Aprendizaje para una Asignatura.
     * Rol: Docente, Coordinador
     * CU-RA-001: Configurar RA por Asignatura
     * @param dto Datos del RA de asignatura a crear.
     * @return El RA de asignatura creado con su ID.
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('Docente', 'Coordinador')") // Solo docentes y coordinadores pueden crear
    public ResponseEntity<AsignaturaRADTO> createAsignaturaRA(@Valid @RequestBody AsignaturaRACreateUpdateDTO dto) {
        AsignaturaRADTO savedRA = asignaturaRAService.saveAsignaturaRA(dto);
        return new ResponseEntity<>(savedRA, HttpStatus.CREATED);
    }

    /**
     * Obtiene un Resultado de Aprendizaje de Asignatura por su ID.
     * @param id ID del RA de asignatura.
     * @return El RA de asignatura.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaRADTO> getAsignaturaRAById(@PathVariable Long id) {
        return asignaturaRAService.getAsignaturaRADTOById(id)
                .map(ra -> new ResponseEntity<>(ra, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Obtiene todos los Resultados de Aprendizaje para una asignatura y un docente específicos.
     * @param asignaturaId ID de la asignatura.
     * @param docenteId ID del docente.
     * @return Lista de RA de asignatura.
     */
    @GetMapping("/by-asignatura-docente")
    public ResponseEntity<List<AsignaturaRADTO>> getAsignaturaRAsByAsignaturaAndDocente(
            @RequestParam String asignaturaId,
            @RequestParam String docenteId) {
        List<AsignaturaRADTO> ras = asignaturaRAService.getAsignaturaRAsByAsignaturaAndDocente(asignaturaId, docenteId);
        return new ResponseEntity<>(ras, HttpStatus.OK);
    }

    /**
     * Obtiene todos los Resultados de Aprendizaje gestionados por un docente.
     * @param docenteId ID del docente.
     * @return Lista de RA de asignatura.
     */
    @GetMapping("/by-docente/{docenteId}")
    public ResponseEntity<List<AsignaturaRADTO>> getAsignaturaRAsByDocente(@PathVariable String docenteId) {
        List<AsignaturaRADTO> ras = asignaturaRAService.getAsignaturaRAsByDocente(docenteId);
        return new ResponseEntity<>(ras, HttpStatus.OK);
    }

    /**
     * Actualiza un Resultado de Aprendizaje de Asignatura existente.
     * Rol: Docente, Coordinador
     * CU-RA-001: Configurar RA por Asignatura
     * CU-RA-003: Ajustar RA Seleccionados (Docente)
     * @param id ID del RA de asignatura a actualizar.
     * @param dto Datos actualizados del RA de asignatura.
     * @return El RA de asignatura actualizado.
     */
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

    /**
     * Elimina un Resultado de Aprendizaje de Asignatura.
     * Rol: Coordinador (o Docente de sus propios RA, con lógica de seguridad adicional)
     * @param id ID del RA de asignatura a eliminar.
     * @return Respuesta sin contenido si la eliminación fue exitosa.
     */
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

    /**
     * Permite a un Docente copiar RA de un semestre o periodo anterior.
     * Rol: Docente
     * CU-RA-002: Copiar RA de Semestre/Periodo Anterior
     * @param asignaturaId ID de la asignatura.
     * @param docenteId ID del docente.
     * @param previousSemester El semestre/periodo desde el cual copiar.
     * @return Lista de los nuevos RA copiados para el semestre actual.
     */
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
