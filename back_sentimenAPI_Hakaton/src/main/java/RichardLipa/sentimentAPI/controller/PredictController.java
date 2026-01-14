package RichardLipa.sentimentAPI.controller;


import RichardLipa.sentimentAPI.domain.comentario.DatosRespuestaSentimiento;
import RichardLipa.sentimentAPI.domain.comentario.DatosTextoJson;
import RichardLipa.sentimentAPI.domain.comentario.ErrorMensaje;
import RichardLipa.sentimentAPI.service.SentimientoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/predict")// endPoind
public class PredictController {

    @Autowired
    private SentimientoService service;

    // 1. MANTIENE LA FUNCIONALIDAD JSON ORIGINAL

/*    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> analizarJson(@RequestBody(required = false) List<DatosTextoJson> datos) {
        System.out.println("ejecutando /predict");
        if (datos == null || datos.isEmpty()) {
            System.out.println("peticion en blanco");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMensaje("No se encontraron comentarios para procesar", 400));
        }

        // Si todo está bien, procesamos
        List<DatosRespuestaSentimiento> resultados = service.procesarLista(datos);
        return ResponseEntity.ok(resultados);
    }*/




    // 2. RECIBE UN ARCHIVO CSV Y DEVUELVE JSON


/*    @PostMapping(value = "/upload-csv", consumes = "multipart/form-data")
    public ResponseEntity<?> analizarCsv(@RequestParam("file")  MultipartFile file) {
        try {
            // 1. Validar si el archivo existe
            if (file == null || file.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorMensaje("No se ha seleccionado ningún archivo.", 400));
            }

            // 2. VALIDACIÓN DE TAMAÑO (Máximo 3MB)
            // 3MB = 3 * 1024 * 1024 bytes = 3,145,728 bytes
            long maxSizeBytes = 3 * 1024 * 1024;
            if (file.getSize() > maxSizeBytes) {
                return ResponseEntity
                        .status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body(new ErrorMensaje("El archivo es demasiado grande. El límite permitido es de 3MB.", 413));
            }

            // 3. Validar extensión CSV
            String contentType = file.getContentType();
            String fileName = file.getOriginalFilename();
            boolean isCsv = (contentType != null && contentType.equals("text/csv")) ||
                    (fileName != null && fileName.toLowerCase().endsWith(".csv"));

            if (!isCsv) {
                return ResponseEntity
                        .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(new ErrorMensaje("Formato no válido. Solo se permiten archivos .csv", 415));
            }

            // 4. Procesar datos
            List<DatosTextoJson> datos = service.leerCsv(file);
            if (datos.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorMensaje("El archivo CSV no contiene datos válidos.", 400));
            }

            List<DatosRespuestaSentimiento> resultados = service.procesarLista(datos);
            return ResponseEntity.ok(resultados);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMensaje("Error interno al procesar el CSV: " + e.getMessage(), 500));
        }
    }*/

    // 3. RECIBE UN ARCHIVO CSV Y DEVUELVE OTRO ARCHIVO CSV (EXPORTAR)
/*    @PostMapping(value = "/export-csv", consumes = "multipart/form-data", produces = "text/csv")
    public void exportarCsv(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        // Configuramos el nombre del archivo de salida
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=resultados_sentimiento.csv");

        List<DatosTextoJson> datos = service.leerCsv(file);
        List<DatosRespuestaSentimiento> resultados = service.procesarLista(datos);

        // Llamamos al servicio para escribir los resultados directamente en el flujo de respuesta
        service.escribirCsv(resultados, response.getWriter());
    }
    */

}
