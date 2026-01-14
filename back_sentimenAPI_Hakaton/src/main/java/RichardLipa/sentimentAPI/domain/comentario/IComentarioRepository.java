package RichardLipa.sentimentAPI.domain.comentario;

import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IComentarioRepository extends JpaRepository<Comentario, Log> {
    Page<Comentario> findAllByStateTrue(Pageable paginacion);

    // Métodos para contar
    long countByPrevisionAndStateTrue(Tipo prevision);
    long countByStateTrue();
}
