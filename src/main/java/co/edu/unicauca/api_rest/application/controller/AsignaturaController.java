package co.edu.unicauca.api_rest.application.controller;

import co.edu.unicauca.api_rest.application.dto.AsignaturaDTO; // Asume que tienes un DTO para Asignatura
import co.edu.unicauca.api_rest.application.service.AsignaturaService; // Asume que tienes un servicio para Asignatura
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas") // Define la URL base para este controlador
public class AsignaturaController {

    private final AsignaturaService asignaturaService;

    @Autowired
    public AsignaturaController(AsignaturaService asignaturaService) {
        this.asignaturaService = asignaturaService;
    }
    
    @GetMapping
    //@PreAuthorize("hasAnyAuthority('Docente', 'Coordinador')")
    public ResponseEntity<List<AsignaturaDTO>> getAllAsignaturas() {
        System.out.println("Asignaturas");
        List<AsignaturaDTO> asignaturas = asignaturaService.getAllAsignaturas();
        return new ResponseEntity<>(asignaturas, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Docente', 'Coordinador')")
    public ResponseEntity<AsignaturaDTO> createAsignatura(@RequestBody AsignaturaDTO asignaturaDTO) {
        AsignaturaDTO createdAsignatura = asignaturaService.createAsignatura(asignaturaDTO);
        return new ResponseEntity<>(createdAsignatura, HttpStatus.CREATED); // Retorna 201 Created
    }
}