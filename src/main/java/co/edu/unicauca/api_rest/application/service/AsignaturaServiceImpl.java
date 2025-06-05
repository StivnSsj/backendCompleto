package co.edu.unicauca.api_rest.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import co.edu.unicauca.api_rest.application.dto.AsignaturaDTO;
import co.edu.unicauca.api_rest.dominio.model.Asignatura;
import co.edu.unicauca.api_rest.dominio.repositories.AsignaturaRepository;

@Service
public class AsignaturaServiceImpl implements AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;

    @Autowired
    public AsignaturaServiceImpl(AsignaturaRepository asignaturaRepository) {
        this.asignaturaRepository = asignaturaRepository;
    }

    @Override
    public List<AsignaturaDTO> getAllAsignaturas() {
        List<Asignatura> asignaturas = asignaturaRepository.findAll();
        return asignaturas.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AsignaturaDTO createAsignatura(AsignaturaDTO asignaturaDTO) {
        // Validación: Verifica si una asignatura con el mismo ID ya existe
        if (asignaturaRepository.existsById(asignaturaDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Asignatura con ID " + asignaturaDTO.getId() + " ya existe.");
        }

        Asignatura asignatura = mapToEntity(asignaturaDTO);
        Asignatura savedAsignatura = asignaturaRepository.save(asignatura);
        return mapToDTO(savedAsignatura);
    }

    // Método auxiliar para mapear DTO a Entidad (necesario para la creación)
    private Asignatura mapToEntity(AsignaturaDTO dto) {
        Asignatura asignatura = new Asignatura();
        asignatura.setId(dto.getId()); // El ID viene del DTO, ya que no es auto-generado en el esquema
        asignatura.setNombre(dto.getNombre());
        asignatura.setDescripcion(dto.getDescripcion());
        asignatura.setCreditos(dto.getCreditos());
        asignatura.setSemestre(dto.getSemestre());
        return asignatura;
    }

    // Método auxiliar para mapear Entidad a DTO
    private AsignaturaDTO mapToDTO(Asignatura asignatura) {
        AsignaturaDTO dto = new AsignaturaDTO();
        dto.setId(asignatura.getId());
        dto.setNombre(asignatura.getNombre());
        dto.setDescripcion(asignatura.getDescripcion());
        dto.setCreditos(asignatura.getCreditos());
        dto.setSemestre(asignatura.getSemestre());
        return dto;
    }
}