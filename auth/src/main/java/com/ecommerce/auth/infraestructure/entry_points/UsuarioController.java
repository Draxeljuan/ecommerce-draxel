package com.ecommerce.auth.infraestructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.infraestructure.entry_points.dto.LoginDTO;
import com.ecommerce.auth.infraestructure.entry_points.dto.UsuarioDTO;
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
    public ResponseEntity<UsuarioDTO> saveUsuario(@RequestBody UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioMapper.dtoToUsuario(usuarioDTO);
        Usuario usuarioValidadoGuardado = usuarioUseCase.guardarUsuario(usuario);
        return new ResponseEntity<>(usuarioMapper.usuarioToDto(usuarioValidadoGuardado), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findByIdUsuario(@PathVariable String id){
        Usuario usuario = usuarioUseCase.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuarioMapper.usuarioToDto(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
        String mensajeAuth = usuarioUseCase.login(loginDTO.email(), loginDTO.pass());
        return ResponseEntity.ok(mensajeAuth);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id){
        usuarioUseCase.eliminarUsuario(id);
        return ResponseEntity.ok().body("Usuario eliminado exitosamente");
    }

    @PutMapping("/update")
    public ResponseEntity<UsuarioDTO> updateUsuario(@RequestBody UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioMapper.dtoToUsuario(usuarioDTO);
        Usuario usuarioValidadoActualizado = usuarioUseCase.actualizarUsuario(usuario);
        return ResponseEntity.ok(usuarioMapper.usuarioToDto(usuarioValidadoActualizado));
    }


}
