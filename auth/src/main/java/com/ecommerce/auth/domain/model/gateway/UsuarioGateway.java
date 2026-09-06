package com.ecommerce.auth.domain.model.gateway;

import com.ecommerce.auth.domain.model.Usuario;


// Son aquellos metodos que nos permiten conectar el dominio con la infraestructura

public interface UsuarioGateway {

    // Contratos, lo que retorna, los parametros de la funcion y lo que recibe

    Usuario guardarUsuario(Usuario usuario);

    Usuario buscarPorId(String idUsuario);

    Usuario actualizarUsuario (Usuario usuario);

    void eliminarUsuario(String idUsuario);


}
