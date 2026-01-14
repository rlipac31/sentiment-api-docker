package RichardLipa.sentimentAPI.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer { // Cambié el nombre para evitar conflictos con Spring
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // PASO 1: Pasa todos los dominios en una sola llamada separados por coma
                .allowedOrigins("http://localhost:3000","https://fron-sentiment-api-hlnd.vercel.app/", "https://fron-sentiment-api.vercel.app")

                // PASO 2: Métodos permitidos
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                // PASO 3: Headers (Añadimos "*" para evitar que falte alguno de Next.js)
                .allowedHeaders("*")

                // PASO 4: Credenciales (Obligatorio si usas Bearer Tokens o Cookies)
                .allowCredentials(true)

                .maxAge(3600);
    }
}

