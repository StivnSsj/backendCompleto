package co.edu.unicauca.api_rest.dominio.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asignaturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "asignaturasDocentes"})

public class Asignatura {
    @Id
    private String id; // VARCHAR(50) PRIMARY KEY

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "creditos")
    private Integer creditos;

    @Column(name = "semestre")
    private Integer semestre;

    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonIgnore // Podrías usar esto aquí si NUNCA quieres serializar esta colección desde Asignatura
    private Set<AsignaturaDocente> asignaturasDocentes = new HashSet<>();
}