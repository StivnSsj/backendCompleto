package co.edu.unicauca.api_rest.application.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unicauca.api_rest.application.dto.AsignaturaDTO;
import co.edu.unicauca.api_rest.dominio.model.Asignatura;
import co.edu.unicauca.api_rest.dominio.model.AsignaturaDocente;
import co.edu.unicauca.api_rest.dominio.model.Docente;
import co.edu.unicauca.api_rest.dominio.repositories.AsignaturaDocenteRepository;
import co.edu.unicauca.api_rest.dominio.repositories.AsignaturaRepository;
import co.edu.unicauca.api_rest.dominio.repositories.DocenteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsignacionDocenteAsignaturaService {

    private final AsignaturaRepository asignaturaRepository;
    private final DocenteRepository docenteRepository;
    private final AsignaturaDocenteRepository asignaturaDocenteRepository;

    @Autowired
    public AsignacionDocenteAsignaturaService(AsignaturaRepository asignaturaRepository,
                                            DocenteRepository docenteRepository,
                                            AsignaturaDocenteRepository asignaturaDocenteRepository) {
        this.asignaturaRepository = asignaturaRepository;
        this.docenteRepository = docenteRepository;
        this.asignaturaDocenteRepository = asignaturaDocenteRepository;
    }

    /**
     * Asigna un docente a una asignatura para un semestre específico.
     * Si ya existe la asignación para ese semestre, la ignora o la actualiza si es necesario.
     */
    @Transactional
    public AsignaturaDocente asignarDocenteAAsignatura(String asignaturaId, Long docenteId, String semestreAcademico, Boolean esPrincipal) {
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada con ID: " + asignaturaId));
        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con ID: " + docenteId));

        // Verificar si la asignación ya existe para evitar duplicados
        return asignaturaDocenteRepository.findByAsignaturaIdAndDocenteIdAndSemestreAcademico(asignaturaId, docenteId, semestreAcademico)
            .map(existingAssignment -> {
                // Si existe, puedes decidir actualizarla o simplemente retornar la existente
                existingAssignment.setEsPrincipal(esPrincipal);
                return asignaturaDocenteRepository.save(existingAssignment);
            })
            .orElseGet(() -> {
                // Si no existe, crear una nueva
                AsignaturaDocente nuevaAsignacion = new AsignaturaDocente(asignatura, docente, semestreAcademico, esPrincipal);
                // JPA manejará la relación bidireccional si usas los métodos add/remove en las entidades (opcional)
                // asignatura.addAsignaturaDocente(nuevaAsignacion);
                // docente.addAsignaturaDocente(nuevaAsignacion);
                return asignaturaDocenteRepository.save(nuevaAsignacion);
            });
    }

    /**
     * Obtiene todas las asignaturas que imparte un docente específico en un semestre dado.
     */
     @Transactional(readOnly = true)
    public List<AsignaturaDTO> getAsignaturasByDocenteAndSemestre(Long docenteId, String semestreAcademico) {
        List<AsignaturaDocente> asignaciones = asignaturaDocenteRepository.findByDocenteId(docenteId);

        return asignaciones.stream()
                .filter(ad -> ad.getSemestreAcademico().equals(semestreAcademico))
                .map(AsignaturaDocente::getAsignatura) // Obtiene el objeto Asignatura
                .map(asignatura -> new AsignaturaDTO( // Mapea a AsignaturaDTO
                    asignatura.getId(),
                    asignatura.getNombre(),
                    asignatura.getDescripcion(),
                    asignatura.getCreditos(),
                    asignatura.getSemestre()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los docentes que imparten una asignatura específica en un semestre dado.
     */
    @Transactional(readOnly = true)
    public List<Docente> getDocentesByAsignaturaAndSemestre(String asignaturaId, String semestreAcademico) {
        List<AsignaturaDocente> asignaciones = asignaturaDocenteRepository.findByAsignaturaId(asignaturaId);
        return asignaciones.stream()
                .filter(ad -> ad.getSemestreAcademico().equals(semestreAcademico))
                .map(AsignaturaDocente::getDocente)
                .collect(Collectors.toList());
    }

    // Otros métodos como desasignar, actualizar, etc.
}
