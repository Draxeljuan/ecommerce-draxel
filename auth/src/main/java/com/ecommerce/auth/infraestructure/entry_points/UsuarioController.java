package com.ecommerce.auth.infraestructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.infraestructure.driver_adapters.UsuarioDTO;
import com.ecommerce.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ecommerce/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/save")
    public ResponseEntity<UsuarioDTO> saveUsuario(@RequestBody UsuarioDTO usuarioDto){

        Usuario usuario = usuarioMapper.dtoToUsuario(usuarioDto);
        Usuario usuarioValidadoGuardado = usuarioUseCase.guardarUsuario(usuario);

        if(usuarioValidadoGuardado.getIdUsuario() != null){
            UsuarioDTO respuestaDto = usuarioMapper.usuarioToDto(usuarioValidadoGuardado);
            return new ResponseEntity<>(respuestaDto, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(usuarioMapper.usuarioToDto(usuarioValidadoGuardado), HttpStatus.CONFLICT);
    }
}
