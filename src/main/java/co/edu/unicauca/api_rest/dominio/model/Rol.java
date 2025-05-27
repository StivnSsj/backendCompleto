package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    @Id
    private String id; // VARCHAR(50) PRIMARY KEY

    @Column(name = "nombre", unique = true, nullable = false, length = 100)
    private String nombre; // Ej: ROL_COORDINADOR, ROL_DOCENTE
}