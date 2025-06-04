package co.edu.unicauca.api_rest.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<AsignaturaDTO> getAllAsignaturas() {
        List<Asignatura> asignaturas = asignaturaRepository.findAll();
        // Mapea la lista de entidades Asignatura a una lista de AsignaturaDTOs
        return asignaturas.stream()
                .map(this::mapToDTO) // Usa un método auxiliar para mapear
                .collect(Collectors.toList());
    }

    // Método auxiliar para mapear la entidad a DTO
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
