package co.edu.unicauca.api_rest.dominio.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "docentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "asignaturasDocentes"})

public class Docente {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;

    private String nombres;
    private String apellidos;
    private String tipoIdentificacion;
    private String identificacion;
    private String tipoDocente;

    @Column(unique = true, nullable = false)
    private String correoInstitucional;

    private String ultimoTitulo;

     @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonIgnore // Podrías usar esto aquí si NUNCA quieres serializar esta colección desde Docente
    private Set<AsignaturaDocente> asignaturasDocentes = new HashSet<>();

}