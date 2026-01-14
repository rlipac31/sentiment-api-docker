package RichardLipa.sentimentAPI.controller;


import RichardLipa.sentimentAPI.domain.usuario.DatosAuthentication;
import RichardLipa.sentimentAPI.domain.usuario.IUsuarioRepository;
import RichardLipa.sentimentAPI.domain.usuario.Usuario;
import RichardLipa.sentimentAPI.infra.DatosToken;
import RichardLipa.sentimentAPI.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid DatosAuthentication datos){
        System.out.println("Logueandose.....:::::  ");

        var usuarioExiste = usuarioRepository.findByEmailAndStateTrue(datos.email());
        // Primero verificamso en ls BD exista este usuario y si esta habilitado
        if(usuarioExiste == null){
            String mensage = "⚠️ ⚠️El  Usuario no resgistrado o desabilitado.";
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensage);
        }

        var authenticationToken = new UsernamePasswordAuthenticationToken(datos.email(), datos.contrasenia());

        //guardamo ena authentication los datos que nos devuelve el UserpassAuthenToken
        var authentication = manager.authenticate(authenticationToken);System.out.println(" sigue el procesoo..... ");
        // generamos el token
        var tokenJWT = tokenService.generarToken( (Usuario) authentication.getPrincipal());//no traemos a la entidad usuaro y extraemos el getPrincipal(email,contraseña ...etc)
        System.out.println("token:::  "+ tokenJWT);
        if(tokenJWT != null) {
            return  ResponseEntity.ok(new DatosToken(tokenJWT));

        }
        String mensage = "⚠️ ⚠️El  Usuario no pudo generar su token.";
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensage);

    }

}
