package RichardLipa.sentimentAPI.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosAuthentication(
        @NotNull @NotBlank String email,
        @NotNull  String contrasenia
) {
}
