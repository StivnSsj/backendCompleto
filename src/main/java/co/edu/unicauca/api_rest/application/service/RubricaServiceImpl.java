package co.edu.unicauca.api_rest.application.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unicauca.api_rest.application.dto.CriterioEvaluacionDTO;
import co.edu.unicauca.api_rest.application.dto.NivelDesempenoDTO;
import co.edu.unicauca.api_rest.application.dto.RubricaCreateUpdateDTO;
import co.edu.unicauca.api_rest.application.dto.RubricaDTO;
import co.edu.unicauca.api_rest.dominio.model.AsignaturaRA;
import co.edu.unicauca.api_rest.dominio.model.CriterioEvaluacion;
import co.edu.unicauca.api_rest.dominio.model.NivelDesempeno;
import co.edu.unicauca.api_rest.dominio.model.Rubrica;
import co.edu.unicauca.api_rest.dominio.repositories.AsignaturaRARepository;
import co.edu.unicauca.api_rest.dominio.repositories.RubricaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RubricaServiceImpl implements RubricaService {

    private final RubricaRepository rubricaRepository;
    private final AsignaturaRARepository asignaturaRARepository;

    @Autowired
    public RubricaServiceImpl(RubricaRepository rubricaRepository, AsignaturaRARepository asignaturaRARepository) {
        this.rubricaRepository = rubricaRepository;
        this.asignaturaRARepository = asignaturaRARepository;
    }

    @Override
    @Transactional
    public RubricaDTO createRubrica(RubricaCreateUpdateDTO dto) {
        Rubrica rubrica = toEntity(dto);
        Rubrica savedRubrica = rubricaRepository.save(rubrica);
        return toDto(savedRubrica);
    }

    @Override
    @Transactional
    public RubricaDTO updateRubrica(Long id, RubricaCreateUpdateDTO dto) {
        Rubrica existingRubrica = rubricaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rúbrica no encontrada con ID: " + id));

        existingRubrica.setNombre(dto.getNombre());

        // Actualizar la relación con AsignaturaRA (si es necesario)
        if (!existingRubrica.getAsignaturaRa().getId().equals(dto.getAsignaturaRaId())) {
            AsignaturaRA asignaturaRA = asignaturaRARepository.findById(dto.getAsignaturaRaId())
                    .orElseThrow(() -> new EntityNotFoundException("AsignaturaRA no encontrada con ID: " + dto.getAsignaturaRaId()));
            existingRubrica.setAsignaturaRa(asignaturaRA);
        }

        // Manejar actualización de criterios y niveles (más complejo)
        // La estrategia más simple es eliminar los viejos y añadir los nuevos,
        // o comparar y actualizar/eliminar/añadir inteligentemente.
        // Para simplificar ahora, si hay cambios en criterios, se borran y se vuelven a añadir.
        // En un escenario real, deberías comparar y actualizar para mantener IDs si es posible.
        existingRubrica.getCriterios().clear(); // Borra los criterios existentes
        if (dto.getCriterios() != null) {
            dto.getCriterios().forEach(criterioDto -> {
                CriterioEvaluacion criterio = toCriterioEntity(criterioDto);
                criterio.setRubrica(existingRubrica); // Asegura el vínculo
                existingRubrica.getCriterios().add(criterio); // Añade el nuevo criterio
            });
        }

        Rubrica updatedRubrica = rubricaRepository.save(existingRubrica);
        return toDto(updatedRubrica);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RubricaDTO> getRubricaById(Long id) {
        return rubricaRepository.findById(id).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RubricaDTO> getRubricasByAsignaturaRA(Long asignaturaRaId) {
        // En realidad, RubricaRepository no tiene este método directo
        // Se podría añadir findByAsignaturaRaId(Long id) si es necesario,
        // o si Rubrica tiene una lista de Rubricas, filtrarlo
        return rubricaRepository.findAll().stream() // Esto es ineficiente, mejor crear un método en el repo
                .filter(rubrica -> rubrica.getAsignaturaRa() != null && rubrica.getAsignaturaRa().getId().equals(asignaturaRaId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRubrica(Long id) {
        if (!rubricaRepository.existsById(id)) {
            throw new EntityNotFoundException("Rúbrica no encontrada con ID: " + id);
        }
        rubricaRepository.deleteById(id);
    }

    // --- Métodos de Mapeo (Internal) ---
    @Override
    public Rubrica toEntity(RubricaCreateUpdateDTO dto) {
        Rubrica rubrica = new Rubrica();
        rubrica.setNombre(dto.getNombre());

        AsignaturaRA asignaturaRA = asignaturaRARepository.findById(dto.getAsignaturaRaId())
                .orElseThrow(() -> new EntityNotFoundException("AsignaturaRA no encontrada con ID: " + dto.getAsignaturaRaId()));
        rubrica.setAsignaturaRa(asignaturaRA);

        if (dto.getCriterios() != null) {
            rubrica.setCriterios(dto.getCriterios().stream()
                    .map(criterioDto -> {
                        CriterioEvaluacion criterio = toCriterioEntity(criterioDto);
                        criterio.setRubrica(rubrica); // Establece la referencia bidireccional
                        return criterio;
                    })
                    .collect(Collectors.toList()));
        }
        return rubrica;
    }

    private CriterioEvaluacion toCriterioEntity(CriterioEvaluacionDTO dto) {
        CriterioEvaluacion criterio = new CriterioEvaluacion();
        criterio.setDescripcion(dto.getDescripcion());
        criterio.setPonderacion(dto.getPonderacion());

        if (dto.getNiveles() != null) {
            criterio.setNiveles(dto.getNiveles().stream()
                    .map(nivelDto -> {
                        NivelDesempeno nivel = toNivelEntity(nivelDto);
                        nivel.setCriterio(criterio); // Establece la referencia bidireccional
                        return nivel;
                    })
                    .collect(Collectors.toList()));
        }
        return criterio;
    }

    private NivelDesempeno toNivelEntity(NivelDesempenoDTO dto) {
        NivelDesempeno nivel = new NivelDesempeno();
        nivel.setNombre(dto.getNombre());
        nivel.setPuntuacion(dto.getPuntuacion());
        nivel.setDescripcion(dto.getDescripcion());
        return nivel;
    }

    @Override
    public RubricaDTO toDto(Rubrica entity) {
        RubricaDTO dto = new RubricaDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setAsignaturaRaId(entity.getAsignaturaRa() != null ? entity.getAsignaturaRa().getId() : null);
        dto.setAsignaturaRaDescripcion(entity.getAsignaturaRa() != null ? entity.getAsignaturaRa().getDescripcion() : null);

        if (entity.getCriterios() != null) {
            dto.setCriterios(entity.getCriterios().stream()
                    .map(this::toCriterioDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private CriterioEvaluacionDTO toCriterioDto(CriterioEvaluacion entity) {
        CriterioEvaluacionDTO dto = new CriterioEvaluacionDTO();
        dto.setId(entity.getId());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPonderacion(entity.getPonderacion());

        if (entity.getNiveles() != null) {
            dto.setNiveles(entity.getNiveles().stream()
                    .map(this::toNivelDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private NivelDesempenoDTO toNivelDto(NivelDesempeno entity) {
        NivelDesempenoDTO dto = new NivelDesempenoDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setPuntuacion(entity.getPuntuacion());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }
}
