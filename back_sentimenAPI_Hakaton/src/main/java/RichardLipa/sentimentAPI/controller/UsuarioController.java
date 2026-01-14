package RichardLipa.sentimentAPI.controller;

import RichardLipa.sentimentAPI.domain.usuario.*;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private IUsuarioRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @PostMapping
     public ResponseEntity registroUsuario(@RequestBody @Valid DatosRegistroUsuario datos, UriComponentsBuilder uriComponentsBuilder){
        System.out.println("Creando uisuarioss::::: ");
        String password = passwordEncoder.encode(datos.contrasenia());
        var usuario = new Usuario(datos);
        usuario.setEmail(datos.email());
        usuario.setContrasenia(password);
        userRepository.save(usuario);
        var uri = uriComponentsBuilder.path("usuarios/{id}").buildAndExpand(usuario.getUsername()).toUri();
        System.out.println("URI ..." + uri);
        return ResponseEntity.created(uri).body(new DatosDetalleUsuario(usuario));
    }

    @GetMapping
     public  ResponseEntity<Page<DatosListaUsuarios>> listarUsuarios(@PageableDefault(size = 10, sort = {"nombre"}, page = 0)Pageable paginacion){
        var page = userRepository.findAllByStateTrue(paginacion).map(DatosListaUsuarios:: new);
        return  ResponseEntity.ok(page);
    }
}
