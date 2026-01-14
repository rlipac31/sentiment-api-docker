package RichardLipa.sentimentAPI.domain.comentario;

import RichardLipa.sentimentAPI.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DatosRegistroComentario(

       @NotBlank String texto,
        Tipo prevision,
        Float provabilidad

) {
}
