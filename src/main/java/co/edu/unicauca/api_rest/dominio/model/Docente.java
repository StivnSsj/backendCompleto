package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "docentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Docente {

    // Opción A: Clave primaria compartida con Usuario (más limpia si 1:1)
    @Id
    private Long id; // El ID de este docente es el mismo que el ID del Usuario asociado

    @OneToOne
    @MapsId // Indica que la PK de Docente es también una FK que referencia a Usuario
    @JoinColumn(name = "id") // La columna 'id' en 'docentes' es también FK a 'usuarios.id'
    private Usuario usuario; // Referencia al usuario asociado

    // Si tu `data.sql` usa IDs String como 'DOCE001', entonces tendrías que cambiar esto a String
    // y gestionar la generación de ese ID String en el servicio de registro.
    // Ej: @Id private String id;

    private String nombres;
    private String apellidos;
    private String tipoIdentificacion;
    private String identificacion;
    private String tipoDocente;

    @Column(unique = true, nullable = false)
    private String correoInstitucional; // Debería coincidir con el correo del usuario

    private String ultimoTitulo;

}