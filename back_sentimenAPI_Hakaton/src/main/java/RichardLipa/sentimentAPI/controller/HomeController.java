package RichardLipa.sentimentAPI.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

    @RestController
    @RequestMapping("/")// endPoind
    public class HomeController {
        @GetMapping
        public Map<String, Object> home() {
            System.out.println("Ejecutando Home Controler........");
            // Usamos LinkedHashMap para mantener el orden de inserción
            Map<String, Object> response = new LinkedHashMap<>();

            // 1. Status primero
            response.put("status", "ONLINE");
            response.put("message", "La API de Sentimiento está funcionando correctamente en Azure");

            // 2. Endpoints
            Map<String, String> endpoints = new LinkedHashMap<>();

            endpoints.put("analisis_json", "/sentiment");
            endpoints.put("carga_masiva_csv", "/sentiment/upload-csv");
           // endpoints.put("guardando_comentarios","/sentiment/save-cometario");
            endpoints.put("estadisticas", "/stats");
            response.put("endpoints", endpoints);

            // 3. Formato JSON (como un objeto real, no texto)
            response.put("formato_requerido", "Enviar un Body tipo JSON con la siguiente estructura:");

            List<Map<String, String>> ejemplo = new ArrayList<>();
            Map<String, String> itemEjemplo = new LinkedHashMap<>();
            itemEjemplo.put("texto", "comentarios..........");
            ejemplo.add(itemEjemplo);

            // imprimiendo el formato  [ { "texto": "..." } ]
            response.put("En_este_formato", ejemplo);

            return response;
        }
    }
