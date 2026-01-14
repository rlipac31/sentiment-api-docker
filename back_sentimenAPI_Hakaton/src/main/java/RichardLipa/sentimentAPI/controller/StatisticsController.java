package RichardLipa.sentimentAPI.controller;


import RichardLipa.sentimentAPI.domain.comentario.DatosListaComentarios;
import RichardLipa.sentimentAPI.domain.comentario.IComentarioRepository;
import RichardLipa.sentimentAPI.domain.comentario.Tipo;
import RichardLipa.sentimentAPI.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stats")// endPoind
public class StatisticsController {
    @Autowired
    private IComentarioRepository comentarioRepository;
    @Autowired
    private ComentarioService comentarioService;



    @GetMapping
    public ResponseEntity<?> Estadisticas(
            @PageableDefault(size = 20, sort = {"fechaRegistro"}, direction = Sort.Direction.DESC, page=0) Pageable paginacion) {

        // 1. Obtener la página (por ejemplo, los 20 registros solicitados)
        Page<DatosListaComentarios> page = comentarioService.obtenerComentariosPaginados(paginacion);
        List<DatosListaComentarios> listaEnPagina = page.getContent();

        // 2. USO DE LAMBDA: Separar y contar en un solo paso
        Map<Boolean, Long> contador = listaEnPagina.stream()
                .collect(Collectors.partitioningBy(
                        c -> c.prevision() == Tipo.POSITIVO,
                        Collectors.counting()
                ));

        long posi = contador.get(true);
        long nega = contador.get(false);
        int totalEnPagina = listaEnPagina.size();
        System.out.println("psoitvos :: "+ posi);
        System.out.println("negativos  :: "+ nega);

        // 3. Calcular porcentajes de la página actual
        String porcPosi = (totalEnPagina > 0) ? (posi * 100 / totalEnPagina) + "%" : "0%";
        String porcNega = (totalEnPagina > 0) ? (nega * 100 / totalEnPagina) + "%" : "0%";

        // 4. Construir respuesta JSON
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("total_en_pagina", totalEnPagina);
        respuesta.put("positivos", porcPosi);
        respuesta.put("negativos", porcNega);

        // Agregamos el resto de la estructura de Page
        respuesta.put("content", listaEnPagina);
        respuesta.put("totalElements", page.getTotalElements());
        respuesta.put("totalPages", page.getTotalPages());
        respuesta.put("number", page.getNumber());

        return ResponseEntity.ok(respuesta);
    }

}


