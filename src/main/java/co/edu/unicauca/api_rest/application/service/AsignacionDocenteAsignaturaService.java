package co.edu.unicauca.api_rest.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unicauca.api_rest.application.dto.AsignaturaDTO;
import co.edu.unicauca.api_rest.dominio.exceptions.ResourceNotFoundException;
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
     * Si ya existe la asignación para ese semestre, la actualiza si es necesario.
     */
    @Transactional
    public AsignaturaDocente asignarDocenteAAsignatura(String asignaturaId, Long docenteId, String semestreAcademico, Boolean esPrincipal) {
        // Validar existencia de la asignatura y el docente
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                // Cambiamos EntityNotFoundException por ResourceNotFoundException
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada con ID: " + asignaturaId));
        Docente docente = docenteRepository.findById(docenteId)
                // Cambiamos EntityNotFoundException por ResourceNotFoundException
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con ID: " + docenteId));

        // Verificar si la asignación ya existe para evitar duplicados, o actualizarla
        return asignaturaDocenteRepository.findByAsignaturaIdAndDocenteIdAndSemestreAcademico(asignaturaId, docenteId, semestreAcademico)
            .map(existingAssignment -> {
                // Si existe, y los valores son los mismos, simplemente la devolvemos para evitar una actualización innecesaria.
                if (existingAssignment.getEsPrincipal().equals(esPrincipal)) {
                    // Opcional: Podrías lanzar un BadRequestException si no quieres "actualizar" si no hay cambios.
                    // throw new BadRequestException("La asignación ya existe con los mismos parámetros.");
                    return existingAssignment; // Ya existe con los mismos valores, no se actualiza
                }
                // Si existe pero 'esPrincipal' es diferente, la actualizamos
                existingAssignment.setEsPrincipal(esPrincipal);
                return asignaturaDocenteRepository.save(existingAssignment);
            })
            .orElseGet(() -> {
                // Si no existe, crear una nueva asignación
                AsignaturaDocente nuevaAsignacion = new AsignaturaDocente(asignatura, docente, semestreAcademico, esPrincipal);
                return asignaturaDocenteRepository.save(nuevaAsignacion);
            });
    }

    /**
     * Obtiene todas las asignaturas que imparte un docente específico en un semestre dado.
     */
    @Transactional(readOnly = true)
    public List<AsignaturaDTO> getAsignaturasByDocenteAndSemestre(Long docenteId, String semestreAcademico) {
        // Validación de existencia del docente
        if (!docenteRepository.existsById(docenteId)) {
            throw new ResourceNotFoundException("Docente no encontrado con ID: " + docenteId);
        }

        // Obtener asignaciones para el docente. Filtrar por semestre.
        // Es importante que la relación AsignaturaDocente a Asignatura y Docente no sea LAZY si la usamos fuera de la transacción
        // o si es LAZY, que los métodos de mapeo se llamen dentro de esta transacción.
        List<AsignaturaDocente> asignaciones = asignaturaDocenteRepository.findByDocenteIdAndSemestreAcademico(docenteId, semestreAcademico);

        if (asignaciones.isEmpty()) {
            // No se lanza ResourceNotFoundException si no hay asignaturas, simplemente se devuelve una lista vacía.
            // Si consideras que es un error no encontrar ninguna asignación, podrías lanzarla.
            // Para este caso, una lista vacía es una respuesta válida.
            return List.of(); // Devuelve una lista inmutable vacía
        }

        return asignaciones.stream()
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
        // Validación de existencia de la asignatura
        if (!asignaturaRepository.existsById(asignaturaId)) {
            throw new ResourceNotFoundException("Asignatura no encontrada con ID: " + asignaturaId);
        }

        List<AsignaturaDocente> asignaciones = asignaturaDocenteRepository.findByAsignaturaIdAndSemestreAcademico(asignaturaId, semestreAcademico);

         if (asignaciones.isEmpty()) {
            return List.of(); // Devuelve una lista inmutable vacía
        }

        return asignaciones.stream()
                .map(AsignaturaDocente::getDocente)
                .collect(Collectors.toList());
    }

    /**
     * Elimina una asignación específica de docente a asignatura por sus IDs y semestre.
     * @param asignaturaId ID de la asignatura
     * @param docenteId ID del docente
     * @param semestreAcademico Semestre académico de la asignación
     */
    @Transactional
    public void desasignarDocenteAAsignatura(String asignaturaId, Long docenteId, String semestreAcademico) {
        // Valida que la asignación exista antes de intentar eliminarla
        AsignaturaDocente asignacion = asignaturaDocenteRepository.findByAsignaturaIdAndDocenteIdAndSemestreAcademico(
                asignaturaId, docenteId, semestreAcademico)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de docente a asignatura no encontrada para Asignatura ID: " + asignaturaId + ", Docente ID: " + docenteId + " y Semestre: " + semestreAcademico));

        asignaturaDocenteRepository.delete(asignacion);
    }

    // Otros métodos auxiliares o de negocio si son necesarios
}