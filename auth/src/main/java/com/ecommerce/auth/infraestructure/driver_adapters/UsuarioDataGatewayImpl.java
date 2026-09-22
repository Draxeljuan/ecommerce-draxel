package com.ecommerce.auth.infraestructure.driver_adapters;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class UsuarioDataGatewayImpl implements UsuarioGateway {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioDataJpaRepository usuarioDataJpaRepository;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        UsuarioData usuarioData = usuarioMapper.toUsuarioData(usuario);
        return usuarioMapper.toUsuario(usuarioDataJpaRepository.save(usuarioData));
    }

    @Override
    public void eliminarUsuario(String id) {
        try {
            usuarioDataJpaRepository.deleteById(id);
        } catch (Exception error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    @Override
    public Usuario buscarPorId(String id) {
        return usuarioDataJpaRepository.findById(id)
                .map(usuarioMapper::toUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        UsuarioData usuarioData = usuarioMapper.toUsuarioData(usuario);

        if(!usuarioDataJpaRepository.existsById(usuario.getIdUsuario())){
            throw new RuntimeException("Usuario con id " + usuario.getIdUsuario() + " no existe");
        }
        return usuarioMapper.toUsuario(usuarioDataJpaRepository.save(usuarioData));
    }


}