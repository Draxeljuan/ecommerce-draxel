package com.ecommerce.auth.domain.usecase;

import com.ecommerce.auth.domain.exception.BusinessRuleException;
import com.ecommerce.auth.domain.exception.UserNotFoundException;
import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class UsuarioUseCase {

    // Se implementa la lógica de negocio de las API
    private final UsuarioGateway usuarioGateway;

    // Caso de uso guardar usuario

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new BusinessRuleException("El usuario recibido no puede ser nulo en todos su campos");
        }

        // Validación menor de edad
        if (usuario.getEdad() == null) {
            throw new BusinessRuleException("La edad no puede ser nula");
        } else if (usuario.getEdad() < 18) {
            throw new BusinessRuleException("Es menor de edad");
        }

        // Validación del resto de campos
        boolean hayCamposIncompletos = Stream.of(
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPass(),
                usuario.getRole(),
                usuario.getNumeroTelefono()
        ).anyMatch(valor -> valor == null || valor.trim().isEmpty());

        if (hayCamposIncompletos) {
            throw new BusinessRuleException("Todos los campos deben estar completos.");
        }

        // Validación Longitud Pass e email
        if (usuario.getPass().length() > 12) {
            throw new BusinessRuleException("Contraseña excede tamaño permitido");
        } else if (usuario.getEmail().length() > 30) {
            throw new BusinessRuleException("Email excede tamaño permitido");
        }


        return usuarioGateway.guardarUsuario(usuario);
    }

    public Usuario buscarUsuarioPorId(String idUsuario){

        if (idUsuario == null || idUsuario.trim().isEmpty() ) {
            throw new BusinessRuleException("Para buscar un usuario por su Id, esta ultimo no puede ser nulo");
        }

        Usuario usuarioBuscado = usuarioGateway.buscarPorId(idUsuario);

        if (usuarioBuscado == null){
            throw new UserNotFoundException("Usuario no encontrado");
        }

        return usuarioBuscado;

    }

}
