package RichardLipa.sentimentAPI.domain.usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record DatosRegistroUsuario(
        @NotBlank String nombre,
        @NotBlank String email,
        @NotBlank String contrasenia,
        @NotNull Role role
        ) {
}
