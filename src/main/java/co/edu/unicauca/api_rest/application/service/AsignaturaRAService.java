package co.edu.unicauca.api_rest.application.service;


import java.util.List;
import java.util.Optional;

import co.edu.unicauca.api_rest.application.dto.AsignaturaRACreateUpdateDTO;
import co.edu.unicauca.api_rest.application.dto.AsignaturaRADTO;
import co.edu.unicauca.api_rest.dominio.model.AsignaturaRA;

public interface AsignaturaRAService {
    AsignaturaRADTO saveAsignaturaRA(AsignaturaRACreateUpdateDTO dto);
    Optional<AsignaturaRADTO> getAsignaturaRADTOById(Long id);
    List<AsignaturaRADTO> getAsignaturaRAsByAsignaturaAndDocente(String asignaturaId, Long docenteId);
    List<AsignaturaRADTO> getAsignaturaRAsByDocente(Long docenteId);
    AsignaturaRADTO updateAsignaturaRA(Long id, AsignaturaRACreateUpdateDTO dto);
    void deleteAsignaturaRA(Long id);
    List<AsignaturaRADTO> copyAsignaturaRAsFromPreviousSemester(String asignaturaId, Long docenteId, String previousSemester);

    // Métodos para mapeo o internos, si los requieres en la interfaz
    AsignaturaRA toEntity(AsignaturaRACreateUpdateDTO dto);
    AsignaturaRADTO toDto(AsignaturaRA entity);
}
