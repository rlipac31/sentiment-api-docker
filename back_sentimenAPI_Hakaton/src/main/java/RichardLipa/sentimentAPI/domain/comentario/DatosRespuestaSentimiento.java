package RichardLipa.sentimentAPI.domain.comentario;

public record DatosRespuestaSentimiento(
        String texto,
        String prevision,
        Double probabilidad
) {
}
