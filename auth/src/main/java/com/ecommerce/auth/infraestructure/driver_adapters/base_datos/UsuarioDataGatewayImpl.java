package com.ecommerce.auth.infraestructure.driver_adapters.base_datos;

import com.ecommerce.auth.domain.exception.ReglaNegocioException;
import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
        } catch (DataIntegrityViolationException e){
            throw new ReglaNegocioException("No se puede eliminar el usuario porque tiene " +
                    "carritos/compras asociadas en el sistema");
        }
    }

    @Override
    public Usuario buscarPorId(String id) {
        return usuarioDataJpaRepository.findById(id)
                .map(usuarioMapper::toUsuario)
                .orElse(null);

    }

    @Override
    public Usuario buscarPorEmail(String email){
        return usuarioDataJpaRepository.findByEmail(email)
                .map(usuarioMapper::toUsuario)
                .orElse(null);
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        UsuarioData usuarioData = usuarioMapper.toUsuarioData(usuario);
        return usuarioMapper.toUsuario(usuarioDataJpaRepository.save(usuarioData));
    }


}