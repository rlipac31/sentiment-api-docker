package RichardLipa.sentimentAPI.domain.usuario;


import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record DatosDetalleUsuario(
         Long id,
         String nombre,
         String email,
         String fechaRegistro
) {
    public DatosDetalleUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getFechaRegistro().format(
                        DateTimeFormatter.ofPattern( "d 'de' MMMM 'del' yyyy 'a las' h:mm a", new Locale("es", "ES"))
                )
        );
    }

}
