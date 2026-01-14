package RichardLipa.sentimentAPI.service;


import RichardLipa.sentimentAPI.domain.comentario.DatosListaComentarios;
import RichardLipa.sentimentAPI.domain.comentario.DatosRespuestaSentimiento;
import RichardLipa.sentimentAPI.domain.comentario.IComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // <--- ¡Esta anotación es crucial!
public class ComentarioService {
    @Autowired
    private SentimientoService service;
    @Autowired
    private IComentarioRepository comentarioRepository;

    public Page<DatosListaComentarios> obtenerComentariosPaginados(Pageable paginacion) {
        return comentarioRepository.findAllByStateTrue(paginacion)
                .map(DatosListaComentarios:: new);
    }




}