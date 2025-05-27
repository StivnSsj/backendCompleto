package co.edu.unicauca.api_rest.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    @NotBlank(message = "El ID del usuario no puede estar vacío")
    @Size(max = 50, message = "El ID del usuario no puede exceder los 50 caracteres")
    private String id; // Aunque podría ser autogenerado si usas Long/UUID, aquí es String como en tu entidad

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 255, message = "El apellido no puede exceder los 255 caracteres")
    private String apellido;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El formato del correo es inválido")
    @Size(max = 255, message = "El correo no puede exceder los 255 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, max = 255, message = "La contraseña debe tener entre 6 y 255 caracteres")
    private String password;

    @NotBlank(message = "El rol no puede estar vacío")
    @Size(max = 50, message = "El ID del rol no puede exceder los 50 caracteres")
    private String rolId; // El ID del rol, ej: 'ROL_DOCENTE'
}
