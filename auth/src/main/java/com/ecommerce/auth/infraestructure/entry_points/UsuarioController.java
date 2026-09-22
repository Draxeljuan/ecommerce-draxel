package com.ecommerce.auth.infraestructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.infraestructure.driver_adapters.UsuarioData;
import com.ecommerce.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecommerce/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/save")
    public ResponseEntity<Usuario> saveUsuario(@RequestBody UsuarioData usuarioData){
        Usuario usuario = usuarioMapper.toUsuario(usuarioData);
        Usuario usuarioValidadoGuardado = usuarioUseCase.guardarUsuario(usuario);

        if(usuarioValidadoGuardado.getIdUsuario() != null){
            return new ResponseEntity<>(usuarioValidadoGuardado, HttpStatus.OK);
        }

        return new ResponseEntity<>(usuarioValidadoGuardado, HttpStatus.CONFLICT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findByIdUsuario(@PathVariable String id){
        Usuario usuarioValidadoEncontrado = usuarioUseCase.buscarUsuarioPorId(id);

        if(usuarioValidadoEncontrado.getIdUsuario() != null){
            return new ResponseEntity<>(usuarioValidadoEncontrado, HttpStatus.OK);
        }

        return new ResponseEntity<>(usuarioValidadoEncontrado, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id){
        try{
            usuarioUseCase.eliminarUsuario(id);
            return ResponseEntity.ok().body("Usuario eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Usuario> updateUsuario(@RequestBody UsuarioData usuarioData){
        try{
            Usuario usuario = usuarioMapper.toUsuario(usuarioData);
            Usuario usuarioValidadActualizado = usuarioUseCase.actualizarUsuario(usuario);
            return new ResponseEntity<>(usuarioValidadActualizado, HttpStatus.OK);
        }catch (Exception error){
            return ResponseEntity.notFound().build();
        }
    }


}
