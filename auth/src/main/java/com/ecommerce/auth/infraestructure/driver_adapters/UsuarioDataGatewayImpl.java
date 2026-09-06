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
    public Usuario guardarUsuario(Usuario usuario){
        UsuarioData usuarioData = usuarioMapper.toUsuarioData(usuario);
        return usuarioMapper.toUsuario(usuarioDataJpaRepository.save(usuarioData));
    }

    @Override
    public Usuario buscarPorId(String idUsuario) {
        return null;
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public void eliminarUsuario(String idUsuario) {

    }

}
