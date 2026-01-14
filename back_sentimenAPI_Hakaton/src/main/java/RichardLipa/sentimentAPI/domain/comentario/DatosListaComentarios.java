package RichardLipa.sentimentAPI.domain.comentario;

import RichardLipa.sentimentAPI.domain.usuario.Usuario;
import lombok.extern.java.Log;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record DatosListaComentarios(
        Long id,
        String comentario,
        Tipo prevision,
        Float provabilidad,
        String fecharegistro
) {
    public DatosListaComentarios(Comentario comentario) {
        this(
                comentario.getId(),
                comentario.getComentario(),
                comentario.getPrevision(),
                comentario.getProvabilidad(),
                comentario.getFechaRegistro().format(
                        DateTimeFormatter.ofPattern( "d 'de' MMMM 'del' yyyy 'a las' h:mm a", new Locale("es", "ES"))
                )
        );
    }
}
