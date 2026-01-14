package RichardLipa.sentimentAPI.domain.comentario;

import java.util.List;

public record RespuestaClasificacion(
        boolean success,
        int totalAnalizados,
        List<DatosListaComentarios> comentariosClasificados,
        String error // solo se usa si success = false
) {

    // Constructor para éxito
    public RespuestaClasificacion(boolean success, int totalAnalizados, List<DatosListaComentarios> comentariosClasificados) {
        this(success, totalAnalizados, comentariosClasificados, null);
    }
}
