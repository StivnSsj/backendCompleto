package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Rol {
    @Id
    private String id; // Ej. "ROL_DOCENTE"

    @Column(nullable = false, unique = true)
    private String nombre; // Ej. "Docente"

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}