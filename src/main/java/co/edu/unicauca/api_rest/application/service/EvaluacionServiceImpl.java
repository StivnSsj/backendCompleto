package co.edu.unicauca.api_rest.application.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unicauca.api_rest.application.dto.EvaluacionCreateDTO;
import co.edu.unicauca.api_rest.application.dto.EvaluacionDTO;
import co.edu.unicauca.api_rest.application.dto.EvaluacionDetalleDTO;
import co.edu.unicauca.api_rest.dominio.model.CriterioEvaluacion;
import co.edu.unicauca.api_rest.dominio.model.Evaluacion;
import co.edu.unicauca.api_rest.dominio.model.EvaluacionDetalle;
import co.edu.unicauca.api_rest.dominio.model.NivelDesempeno;
import co.edu.unicauca.api_rest.dominio.model.Rubrica;
import co.edu.unicauca.api_rest.dominio.repositories.CriterioEvaluacionRepository;
import co.edu.unicauca.api_rest.dominio.repositories.EvaluacionRepository;
import co.edu.unicauca.api_rest.dominio.repositories.NivelDesempenoRepository;
import co.edu.unicauca.api_rest.dominio.repositories.RubricaRepository;
import co.edu.unicauca.api_rest.dominio.repositories.UsuarioRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class EvaluacionServiceImpl implements EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final RubricaRepository rubricaRepository;
    private final CriterioEvaluacionRepository criterioEvaluacionRepository;
    private final NivelDesempenoRepository nivelDesempenoRepository;
    private final UsuarioRepository usuarioRepository; // Para obtener nombres de usuarios

    @Autowired
    public EvaluacionServiceImpl(EvaluacionRepository evaluacionRepository,
                                 RubricaRepository rubricaRepository,
                                 CriterioEvaluacionRepository criterioEvaluacionRepository,
                                 NivelDesempenoRepository nivelDesempenoRepository,
                                 UsuarioRepository usuarioRepository) {
        this.evaluacionRepository = evaluacionRepository;
        this.rubricaRepository = rubricaRepository;
        this.criterioEvaluacionRepository = criterioEvaluacionRepository;
        this.nivelDesempenoRepository = nivelDesempenoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public EvaluacionDTO createEvaluacion(EvaluacionCreateDTO dto) {
        Evaluacion evaluacion = toEntity(dto);

        // Calcular la puntuación total
        BigDecimal puntuacionTotal = BigDecimal.ZERO;
        for (EvaluacionDetalle detalle : evaluacion.getDetalles()) {
            CriterioEvaluacion criterio = detalle.getCriterio();
            NivelDesempeno nivel = detalle.getNivelSeleccionado();

            if (criterio != null && nivel != null) {
                // Ponderación * Puntuación del nivel
                puntuacionTotal = puntuacionTotal.add(criterio.getPonderacion().multiply(nivel.getPuntuacion()));
            }
        }
        evaluacion.setPuntuacionTotal(puntuacionTotal);

        Evaluacion savedEvaluacion = evaluacionRepository.save(evaluacion);
        return toDto(savedEvaluacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EvaluacionDTO> getEvaluacionById(Long id) {
        return evaluacionRepository.findById(id).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionDTO> getEvaluacionesByEstudianteId(String estudianteId) {
        // Asumiendo que EvaluacionRepository puede tener un método findByEstudianteId
        // Por ahora, filterall es una simulación
        return evaluacionRepository.findAll().stream()
                .filter(eval -> eval.getEstudianteId().equals(estudianteId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionDTO> getEvaluacionesByEvaluadorId(String evaluadorId) {
        // Asumiendo que EvaluacionRepository puede tener un método findByEvaluadorId
        return evaluacionRepository.findAll().stream()
                .filter(eval -> eval.getEvaluadorId().equals(evaluadorId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionDTO> getEvaluacionesByRubricaId(Long rubricaId) {
        // Asumiendo que EvaluacionRepository puede tener un método findByRubricaId
        return evaluacionRepository.findAll().stream()
                .filter(eval -> eval.getRubrica() != null && eval.getRubrica().getId().equals(rubricaId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteEvaluacion(Long id) {
        if (!evaluacionRepository.existsById(id)) {
            throw new EntityNotFoundException("Evaluación no encontrada con ID: " + id);
        }
        evaluacionRepository.deleteById(id);
    }

    // --- Métodos de Mapeo (Internal) ---
    @Override
    public Evaluacion toEntity(EvaluacionCreateDTO dto) {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEstudianteId(dto.getEstudianteId());
        evaluacion.setEvaluadorId(dto.getEvaluadorId());
        evaluacion.setRetroalimentacion(dto.getRetroalimentacion());
        evaluacion.setFechaEvaluacion(LocalDateTime.now()); // Establecer la fecha actual

        Rubrica rubrica = rubricaRepository.findById(dto.getRubricaId())
                .orElseThrow(() -> new EntityNotFoundException("Rúbrica no encontrada con ID: " + dto.getRubricaId()));
        evaluacion.setRubrica(rubrica);

        if (dto.getDetalles() != null) {
            evaluacion.setDetalles(dto.getDetalles().stream()
                    .map(detalleDto -> {
                        EvaluacionDetalle detalle = new EvaluacionDetalle();
                        detalle.setEvaluacion(evaluacion); // Establecer la referencia bidireccional

                        CriterioEvaluacion criterio = criterioEvaluacionRepository.findById(detalleDto.getCriterioId())
                                .orElseThrow(() -> new EntityNotFoundException("Criterio no encontrado con ID: " + detalleDto.getCriterioId()));
                        detalle.setCriterio(criterio);

                        NivelDesempeno nivel = nivelDesempenoRepository.findById(detalleDto.getNivelSeleccionadoId())
                                .orElseThrow(() -> new EntityNotFoundException("Nivel de desempeño no encontrado con ID: " + detalleDto.getNivelSeleccionadoId()));
                        detalle.setNivelSeleccionado(nivel);
                        return detalle;
                    })
                    .collect(Collectors.toList()));
        }
        return evaluacion;
    }

    @Override
    public EvaluacionDTO toDto(Evaluacion entity) {
        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setId(entity.getId());
        dto.setEstudianteId(entity.getEstudianteId());
        dto.setEvaluadorId(entity.getEvaluadorId());
        dto.setFechaEvaluacion(entity.getFechaEvaluacion());
        dto.setPuntuacionTotal(entity.getPuntuacionTotal());
        dto.setRetroalimentacion(entity.getRetroalimentacion());

        if (entity.getRubrica() != null) {
            dto.setRubricaId(entity.getRubrica().getId());
            dto.setRubricaNombre(entity.getRubrica().getNombre());
            if (entity.getRubrica().getAsignaturaRa() != null) {
                dto.setAsignaturaRaId(entity.getRubrica().getAsignaturaRa().getId());
                dto.setAsignaturaRaDescripcion(entity.getRubrica().getAsignaturaRa().getDescripcion());
            }
        }

        // Obtener nombres de usuario (estudiante y evaluador)
        /*usuarioRepository.findById(entity.getEstudianteId())
                .ifPresent(u -> dto.setEstudianteNombre(u.getNombre() + " " + u.getApellido()));
        usuarioRepository.findById(entity.getEvaluadorId())
                .ifPresent(u -> dto.setEvaluadorNombre(u.getNombre() + " " + u.getApellido()));*/

        if (entity.getDetalles() != null) {
            dto.setDetalles(entity.getDetalles().stream()
                    .map(this::toEvaluacionDetalleDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private EvaluacionDetalleDTO toEvaluacionDetalleDto(EvaluacionDetalle entity) {
        EvaluacionDetalleDTO dto = new EvaluacionDetalleDTO();
        dto.setId(entity.getId());
        dto.setCriterioId(entity.getCriterio() != null ? entity.getCriterio().getId() : null);
        dto.setNivelSeleccionadoId(entity.getNivelSeleccionado() != null ? entity.getNivelSeleccionado().getId() : null);
        return dto;
    }
}
