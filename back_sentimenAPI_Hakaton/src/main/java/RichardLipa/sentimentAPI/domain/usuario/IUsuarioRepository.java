package RichardLipa.sentimentAPI.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
  Page<Usuario> findAllByStateTrue(Pageable paginacion);

  UserDetails findByEmail(String email);

    Usuario findByEmailAndStateTrue(String email);

    // Opción 2 (Usando @Query, recomendada para claridad):
   /* @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.state = true")
    Usuario findUsuarioActivoPorEmail(@Param("email") String email);*/


}
