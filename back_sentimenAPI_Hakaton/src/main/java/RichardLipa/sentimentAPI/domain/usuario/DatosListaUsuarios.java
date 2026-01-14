package RichardLipa.sentimentAPI.domain.usuario;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record DatosListaUsuarios(
        Long id,
        String nombre,
        Role role,
        String fechaRegistro
) {

    public DatosListaUsuarios(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getRole(),
                usuario.getFechaRegistro().format(
                        DateTimeFormatter.ofPattern( "d 'de' MMMM 'del' yyyy 'a las' h:mm a", new Locale("es", "ES"))
                )
        );
    }
}
