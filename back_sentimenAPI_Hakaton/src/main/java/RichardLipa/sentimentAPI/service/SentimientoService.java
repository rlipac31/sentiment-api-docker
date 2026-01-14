package RichardLipa.sentimentAPI.service;

import RichardLipa.sentimentAPI.domain.comentario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

//para leer csv

///////
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Writer;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SentimientoService {

    /*
    *
    * ruta de url ser servidor de api python
    * <iframe
	src="https://rlipac-python-api.hf.space"
	frameborder="0"
	width="850"
	height="450"
></iframe>

    *
    * */

    @Autowired
    private IComentarioRepository comentarioRepository;


   // private final String COLAB_URL = "http://127.0.0.1:4000/predict";//servidor local
   private final String COLAB_URL = "https://rlipac-python-api.hf.space/predict";

    public List<DatosRespuestaSentimiento> procesarLista(List<DatosRegistroComentario> datosTexto) {
        System.out.println("texto antes de procesar :::  " + datosTexto);
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("datos:::" + datosTexto);

        // Transformamos cada comentario en una llamada al Colab
        return datosTexto.stream().map(comentario -> {
            try{
                // Creamos el cuerpo que espera Python: {"text": "el texto del comentario"}
                Map<String, String> request = Map.of("texto", comentario.texto());

                // Hacemos el POST al Colab y convertimos la respuesta en nuestro Record
                return restTemplate.postForObject(COLAB_URL, request, DatosRespuestaSentimiento.class);
            } catch (Exception e) {
                System.err.println("ERROR AL CONECTAR CON COLAB: " + e.getMessage());
                return new DatosRespuestaSentimiento("Error de conexión", "", 0.0);
            }

        }).toList();
    }

    public List<DatosRegistroComentario> leerCsv(MultipartFile file) throws Exception {
        List<DatosRegistroComentario> lista = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] linea;
            while ((linea = reader.readNext()) != null) {
                // Suponiendo que el CSV tiene el texto en la primera columna
                lista.add(new DatosRegistroComentario(linea[0].trim(), null, null ));
            }
        }
        return lista;
    }

    ///


    public void escribirCsv(List<DatosRespuestaSentimiento> resultados, Writer writer) throws Exception {
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            // Cabeceras
            csvWriter.writeNext(new String[]{"Texto", "Prevision", "Probabilidad"});

            for (DatosRespuestaSentimiento res : resultados) {
                csvWriter.writeNext(new String[]{
                        res.texto(),
                        res.prevision(),
                        String.valueOf(res.probabilidad())
                });
            }
        }
    }

    @Transactional
    public ResponseEntity<ErrorMensaje> guardarEnBaseDeDatos(List<DatosRespuestaSentimiento> resultados) {

        try {
            if (resultados == null || resultados.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorMensaje("Lista vacía::DESDE SERVOCIO GUARDAR", 400));
            }
            List<Comentario> comentarios = resultados.stream().map(dto -> {
                Comentario comentario = new Comentario();
                comentario.setComentario(dto.texto());
                comentario.setPrevision(Tipo.valueOf(dto.prevision().toUpperCase()));
                // Usamos doubleValue() o floatValue() según tu entidad
                comentario.setProvabilidad(dto.probabilidad().floatValue());
                comentario.setFechaRegistro(LocalDateTime.now());
                comentario.setState(true);
                return comentario;
            }).toList();

            comentarioRepository.saveAll(comentarios);

        }catch (Exception e) {
            // Si la API en la nube falla o hay error de red, cae aquí
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErrorMensaje("Huboo un erro al guardar la lista el la BD : " + e.getMessage(), 503));
        }


        return null;
    }


}



// ... dentro de SentimientoService ...


