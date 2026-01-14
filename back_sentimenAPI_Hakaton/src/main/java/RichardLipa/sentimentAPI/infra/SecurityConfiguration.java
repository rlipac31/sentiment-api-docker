package RichardLipa.sentimentAPI.infra;


import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration// le decemos a sprin que esta es una clase de configuracion
@EnableWebSecurity // le decimos a sprin securiti que hablilite blos cambios de configuracion
public class SecurityConfiguration  {
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    private SecurityFilter securityFilter;// iyectamos nuestra clase

    //creamos un metodo de java sprint security   // sprion injetca de forma automatica los parametros
    // de sucuriada  de sprint security con httpSecuriry
    @Bean//anotacion jpa para que sprint boot puede cargar ese metodo y sorint security lo pueda leer
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
               http
                       .cors(Customizer.withDefaults()); // Esto le dice a Security que use tu CorsConfiguration

      return  http.csrf(csrf->csrf.disable())//desabilitamos el csrf no se necesista para api Rest
              .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//desabilitamos el redireccionamiento a login , ahora carganormalmente kas rutas sin pedir loguearse//ahora nuestro sistema ya no esta en STATEFULL ahora esta en STATELESS
              .authorizeHttpRequests( req ->{
                  System.out.printf("entro a autohrizeFilterr.....    ");
                   req.requestMatchers(HttpMethod.POST, "/login").permitAll();//ruta permitida para todos
                  // ruta sentiment api(recibe los comentarios)
                   req.requestMatchers(HttpMethod.GET, "/").permitAll();//ruta permitida para todos
                  // Acceso a /usuarios solo si tiene el rol ADMIN
                    req.requestMatchers(HttpMethod.POST, "/usuarios").hasRole("ADMIN"); // POST: Crear USUARIOS
                  //req.requestMatchers(HttpMethod.PUT, "/usuarios/{id}").hasRole("ADMIN");  // PUT: actualizar usuario
                  //req.requestMatchers(HttpMethod.DELETE, "/usuarios/{id}").hasRole("ADMIN"); // DELETE: Eliminar médico por ID and PUT acutlizar
                 // req.requestMatchers(HttpMethod.GET, "/usuarios").hasAnyRole("ADMIN","USER"); // GET: Ver médicos (cualquier rol)
                  //req.requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasAnyRole("ADMIN","USER");
                  //req.requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN");
                  //req.requestMatchers(HttpMethod.GET, "/comentarios").hasAnyRole("ADMIN", "USER");
                    // setiment
                  req.requestMatchers(HttpMethod.GET, "/stats").hasAnyRole("ADMIN","USER");//ruta permitida para todos
                  req.requestMatchers(HttpMethod.POST, "/sentiment").hasAnyRole("ADMIN","USER");//ruta permitida para todos/*    req.requestMatchers(HttpMethod.POST, "/sentiment/save-cometario").hasAnyRole("ADMIN","USER");*/
                  req.requestMatchers(HttpMethod.POST, "/sentiment/upload-csv").hasAnyRole("ADMIN","USER");//
                  // req.requestMatchers(HttpMethod.POST, "/sentiment/export-csv").hasAnyRole("ADMIN","USER");//export-csv

                  req.anyRequest().authenticated();//todas las ruras que tienen que ser autenticadas
              })
              // AGREGAR ESTO:
              .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint))
              .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)//agregamos nuestro filtro primero y luego el de sprint security
              .build();//sprin security agrega su filtro primero por defecto, lo cualcausarioa algun error para evitarlo pasamos nuestro filtro primero
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return  configuration.getAuthenticationManager();//devuelve la configuracion AuthenticationManager

    }
 @Bean
 public PasswordEncoder passwordEncoder(){
    // System.out.println("generar contrasña hash....");
     //lee la contrasenia ecriptada
     // el hash es unidireccional,recibe la contrasenia ejmplo: 123456 lo
     // hashea y luego compara ese has con la que esta en la bd
        return  new BCryptPasswordEncoder();
 }
}
