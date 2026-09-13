package com.ecommerce.auth.infraestructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
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
    public ResponseEntity<UsuarioDTO> saveUsuario(@RequestBody UsuarioDTO usuarioDto) {
        Usuario usuario = usuarioMapper.dtoToUsuario(usuarioDto);

        Usuario usuarioValidadoGuardado = usuarioUseCase.guardarUsuario(usuario);

        UsuarioDTO respuestaDto = usuarioMapper.usuarioToDto(usuarioValidadoGuardado);

        return new ResponseEntity<>(respuestaDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuario(@PathVariable String id) {
        Usuario usuarioBuscado = usuarioUseCase.buscarUsuarioPorId(id);

        UsuarioDTO usuarioDTO = usuarioMapper.usuarioToDto(usuarioBuscado);

        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioMapper.dtoToUsuario(usuarioDTO);

        Usuario usuarioValidadoActualizado = usuarioUseCase.actualizarUsuario(usuario);

        UsuarioDTO respuestaDTO = usuarioMapper.usuarioToDto(usuarioValidadoActualizado);

        return new ResponseEntity<>(respuestaDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable String id) {

        usuarioUseCase.eliminarUsuario(id);

        return new ResponseEntity<>("Usuario con id: " + id + " eliminado", HttpStatus.OK);
    }

}
