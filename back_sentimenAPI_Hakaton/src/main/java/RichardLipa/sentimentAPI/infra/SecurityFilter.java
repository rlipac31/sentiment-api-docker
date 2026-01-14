package RichardLipa.sentimentAPI.infra;

import RichardLipa.sentimentAPI.domain.usuario.IUsuarioRepository;
import RichardLipa.sentimentAPI.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var jwtToken = recuperarToken(request);

        try {
            if (jwtToken != null) {
                // Si el token es inválido o expiró, TokenService lanzará una excepción
                var subject = tokenService.getSubject((String) jwtToken);
                var usuario = usuarioRepository.findByEmail(subject);

                if (usuario != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (RuntimeException e) {
            // Guardamos el error específico (ej: "Token expirado") para que el Frontend lo vea
            request.setAttribute("mensaje_error_token", e.getMessage());
        }

        // Continuamos con la cadena. Si no hay autenticación, Spring Security
        // usará el CustomAuthenticationEntryPoint para lanzar el 401.
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}