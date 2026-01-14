package RichardLipa.sentimentAPI.service;


import RichardLipa.sentimentAPI.domain.usuario.IUsuarioRepository;
//import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutheticationService implements UserDetailsService {
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String  email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email);// recibe el nombre de email configurado en el UserDetail en la Entidad Usuario y devuelve un UserDetails
    }
}
