package co.edu.unicauca.api_rest.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    // Datos del Usuario
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser una dirección de email válida")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El rol es obligatorio")
    private String rolId; // Por ejemplo, "ROL_DOCENTE", "ROL_COORDINADOR"

    // Datos específicos del Docente (serán nulos si no es ROL_DOCENTE)
    private String tipoIdentificacion;
    private String identificacion;
    private String tipoDocente;
    private String ultimoTitulo;

    // Getters y Setters
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRolId() {
        return rolId;
    }

    public void setRolId(String rolId) {
        this.rolId = rolId;
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

    public String getUltimoTitulo() {
        return ultimoTitulo;
    }

    public void setUltimoTitulo(String ultimoTitulo) {
        this.ultimoTitulo = ultimoTitulo;
    }
}
