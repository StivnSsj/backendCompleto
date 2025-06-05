package co.edu.unicauca.api_rest.application.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unicauca.api_rest.application.dto.AsignaturaRACreateUpdateDTO;
import co.edu.unicauca.api_rest.application.dto.AsignaturaRADTO;
import co.edu.unicauca.api_rest.dominio.model.Asignatura;
import co.edu.unicauca.api_rest.dominio.model.AsignaturaRA;
import co.edu.unicauca.api_rest.dominio.model.ProgramaRA;
import co.edu.unicauca.api_rest.dominio.repositories.AsignaturaRARepository;
import co.edu.unicauca.api_rest.dominio.repositories.AsignaturaRepository;
import co.edu.unicauca.api_rest.dominio.repositories.DocenteRepository;
import co.edu.unicauca.api_rest.dominio.repositories.ProgramaRARepository;
import co.edu.unicauca.api_rest.dominio.model.Docente;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsignaturaRAServiceImpl implements AsignaturaRAService {

    private final AsignaturaRARepository asignaturaRARepository;
    private final AsignaturaRepository asignaturaRepository; // Para buscar la asignatura
    private final DocenteRepository docenteRepository;       // Para buscar el docente
    private final ProgramaRARepository programaRARepository; // Para buscar el RAP del programa

    @Autowired
    public AsignaturaRAServiceImpl(AsignaturaRARepository asignaturaRARepository,
                                   AsignaturaRepository asignaturaRepository,
                                   DocenteRepository docenteRepository,
                                   ProgramaRARepository programaRARepository) {
        this.asignaturaRARepository = asignaturaRARepository;
        this.asignaturaRepository = asignaturaRepository;
        this.docenteRepository = docenteRepository;
        this.programaRARepository = programaRARepository;
    }

    @Override
    @Transactional // Asegura que la operación sea atómica
    public AsignaturaRADTO saveAsignaturaRA(AsignaturaRACreateUpdateDTO dto) {
        AsignaturaRA asignaturaRA = toEntity(dto);
        AsignaturaRA savedRA = asignaturaRARepository.save(asignaturaRA);
        return toDto(savedRA);
    }

    @Override
    @Transactional(readOnly = true) // Solo lectura, optimización
    public Optional<AsignaturaRADTO> getAsignaturaRADTOById(Long id) {
        return asignaturaRARepository.findById(id).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignaturaRADTO> getAsignaturaRAsByAsignaturaAndDocente(String asignaturaId, Long docenteId) {
        return asignaturaRARepository.findByAsignaturaIdAndDocenteId(asignaturaId, docenteId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignaturaRADTO> getAsignaturaRAsByDocente(Long docenteId) {
        return asignaturaRARepository.findByDocenteId(docenteId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AsignaturaRADTO updateAsignaturaRA(Long id, AsignaturaRACreateUpdateDTO dto) {
        AsignaturaRA existingRA = asignaturaRARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AsignaturaRA no encontrada con ID: " + id));

        // Actualizar campos permitidos
        existingRA.setDescripcion(dto.getDescripcion());
        existingRA.setSemestreAcademico(dto.getSemestreAcademico());
        // Aquí puedes añadir lógica para actualizar la asignatura o el docente si es necesario,
        // pero en un update de un RA específico, los IDs de asignatura/docente rara vez cambian.

        // Actualizar vínculo con ProgramaRA si existe y es diferente
        if (dto.getProgramaRaId() != null && !dto.getProgramaRaId().equals(existingRA.getProgramaRa().getId())) {
             ProgramaRA programaRA = programaRARepository.findById(dto.getProgramaRaId())
                    .orElseThrow(() -> new EntityNotFoundException("ProgramaRA no encontrado con ID: " + dto.getProgramaRaId()));
             existingRA.setProgramaRa(programaRA);
        } else if (dto.getProgramaRaId() == null) {
            existingRA.setProgramaRa(null); // Si el DTO no lo tiene, se desvincula
        }


        AsignaturaRA updatedRA = asignaturaRARepository.save(existingRA);
        return toDto(updatedRA);
    }

    @Override
    @Transactional
    public void deleteAsignaturaRA(Long id) {
        if (!asignaturaRARepository.existsById(id)) {
            throw new EntityNotFoundException("AsignaturaRA no encontrada con ID: " + id);
        }
        asignaturaRARepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<AsignaturaRADTO> copyAsignaturaRAsFromPreviousSemester(String asignaturaId, Long docenteId, String previousSemester) {
        List<AsignaturaRA> previousRAs = asignaturaRARepository.findByAsignaturaId(asignaturaId);

        List<AsignaturaRA> copiedRAs = previousRAs.stream()
                .filter(ra -> ra.getSemestreAcademico().equals(previousSemester))
                .map(ra -> {
                    AsignaturaRA newRA = new AsignaturaRA();
                    // Buscar la asignatura y el docente por sus IDs para asociarlos correctamente
                    Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                            .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada: " + asignaturaId));
                    Docente docente = docenteRepository.findById(docenteId)
                            .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado: " + docenteId));

                    newRA.setAsignatura(asignatura);
                    newRA.setDocente(docente);
                    newRA.setDescripcion(ra.getDescripcion());
                    // Asumiendo que el vínculo al programaRA se mantiene si existe
                    newRA.setProgramaRa(ra.getProgramaRa());
                    // Establecer el semestre actual (esto debería ser dinámico, ej. con un Date/Calendar service)
                    // Por ahora, un valor quemado de ejemplo para el "semestre actual"
                    newRA.setSemestreAcademico("2025-1"); // ¡Cambiar esto por lógica real de semestre actual!
                    return newRA;
                })
                .collect(Collectors.toList());

        // Guardar las nuevas instancias
        List<AsignaturaRA> savedCopiedRAs = asignaturaRARepository.saveAll(copiedRAs);
        return savedCopiedRAs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    // --- Métodos de Mapeo (Internal) ---
    @Override
    public AsignaturaRA toEntity(AsignaturaRACreateUpdateDTO dto) {
        AsignaturaRA entity = new AsignaturaRA();
        entity.setDescripcion(dto.getDescripcion());
        entity.setSemestreAcademico(dto.getSemestreAcademico());

        // Buscar y establecer la relación con Asignatura
        Asignatura asignatura = asignaturaRepository.findById(dto.getAsignaturaId())
                .orElseThrow(() -> new EntityNotFoundException("Asignatura no encontrada con ID: " + dto.getAsignaturaId()));
        entity.setAsignatura(asignatura);

        // Buscar y establecer la relación con Docente
        Docente docente = docenteRepository.findById(dto.getDocenteId())
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con ID: " + dto.getDocenteId()));
        entity.setDocente(docente);

        // Establecer la relación con ProgramaRA si existe
        if (dto.getProgramaRaId() != null && !dto.getProgramaRaId().isEmpty()) {
            ProgramaRA programaRA = programaRARepository.findById(dto.getProgramaRaId())
                    .orElseThrow(() -> new EntityNotFoundException("ProgramaRA no encontrado con ID: " + dto.getProgramaRaId()));
            entity.setProgramaRa(programaRA);
        }
        return entity;
    }

    @Override
    public AsignaturaRADTO toDto(AsignaturaRA entity) {
        AsignaturaRADTO dto = new AsignaturaRADTO();
        dto.setId(entity.getId());
        dto.setAsignaturaId(entity.getAsignatura() != null ? entity.getAsignatura().getId() : null);
        dto.setAsignaturaNombre(entity.getAsignatura() != null ? entity.getAsignatura().getNombre() : null);
        //dto.setDocenteId(entity.getDocente() != null ? entity.getDocente().getId() : null);
        dto.setDocenteNombre(entity.getDocente() != null ? entity.getDocente().getNombres() + " " + entity.getDocente().getApellidos() : null);
        dto.setDescripcion(entity.getDescripcion());
        dto.setSemestreAcademico(entity.getSemestreAcademico());
        dto.setProgramaRaId(entity.getProgramaRa() != null ? entity.getProgramaRa().getId() : null);
        dto.setProgramaRaDescripcion(entity.getProgramaRa() != null ? entity.getProgramaRa().getDescripcion() : null);
        return dto;
    }
}
