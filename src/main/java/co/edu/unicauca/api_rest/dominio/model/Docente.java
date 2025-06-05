package co.edu.unicauca.api_rest.dominio.model;

import jakarta.persistence.*;

@Entity
@Table(name = "docentes")
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

    // Getters y Setters

    public Long getId() { // O String si tu ID es String
        return id;
    }

    public void setId(Long id) { // O String id
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoDocente() {
        return tipoDocente;
    }

    public void setTipoDocente(String tipoDocente) {
        this.tipoDocente = tipoDocente;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getUltimoTitulo() {
        return ultimoTitulo;
    }

    public void setUltimoTitulo(String ultimoTitulo) {
        this.ultimoTitulo = ultimoTitulo;
    }
}