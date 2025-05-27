package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    private String id; // VARCHAR(50) PRIMARY KEY

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 255)
    private String apellido;

    @Column(name = "correo", unique = true, nullable = false, length = 255)
    private String correo;

    @Column(name = "password", nullable = false, length = 255)
    private String password; // NOTA: Aquí se almacenará el hash de la contraseña

    @ManyToOne // Un usuario tiene un rol
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
}