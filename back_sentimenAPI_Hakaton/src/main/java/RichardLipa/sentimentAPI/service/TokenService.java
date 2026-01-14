package RichardLipa.sentimentAPI.service;

import RichardLipa.sentimentAPI.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")// el Value...tiene que ser una anotacion de sprin framework si non no funionara
    private String secret;

    private static final String ISSUER = "sentimentAPI";
        public  String generarToken(Usuario usuario){

            try {
                var algorithm = Algorithm.HMAC256(secret);
                return JWT.create()//devolvemos el token generado
                          .withIssuer(ISSUER)//aqui le pasamo el nombre de la aplicacion o de la empresa dueña de la firma del token
                          .withSubject(usuario.getUsername())//agremamos el nombre de usuario o email(es la credencial de UserDetai9ls
                          .withExpiresAt(fechaDeExpiracion())
                        .sign(algorithm);
            }catch (JWTCreationException exception){
                throw  new RuntimeException("Error No se pudo generar el token:", exception);
            }


        }

    private Instant fechaDeExpiracion() {
        return LocalDateTime.now().plusHours(5).toInstant(ZoneOffset.of("-05:00"));////tiempo de expiracion 4 horas despues de que inicio session
        //zona horaria -7 horas  ....averiguar cuantas horas hay que descontar del meridiano segun su zona horaria

        }

    public String getSubject(String tokenJWT){
            try{
                var algorithm = Algorithm.HMAC256(secret);
                return  JWT.require(algorithm)
                        .withIssuer(ISSUER)
                        .build()
                        .verify(tokenJWT)
                        .getSubject();
            } catch (JWTVerificationException exception) {
                throw new RuntimeException("Token no valido o expirado 01 "+ tokenJWT);
            }
    }
}
