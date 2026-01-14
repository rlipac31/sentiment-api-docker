package RichardLipa.sentimentAPI.domain.comentario;

import jakarta.validation.constraints.NotBlank;

public record DatosTextoJson(
        @NotBlank
        String texto
) {
}
