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

        Usuario usuarioValidado = validacionesUsuario(usuario);

        return usuarioGateway.guardarUsuario(usuarioValidado);
    }

    public Usuario buscarUsuarioPorId(String idUsuario){

        if (idUsuario == null || idUsuario.trim().isEmpty() ) {
            throw new BusinessRuleException("El id no puede ser nulo");
        }

        Usuario usuarioBuscado = usuarioGateway.buscarPorId(idUsuario);

        if (usuarioBuscado == null){
            throw new UserNotFoundException("Usuario no encontrado");
        }

        return usuarioBuscado;

    }

    public Usuario actualizarUsuario(Usuario usuario){

        if (usuario == null) {
            throw new BusinessRuleException("El usuario recibido no puede ser nulo.");
        }

        if (usuario.getIdUsuario() == null) {
            throw new BusinessRuleException("Para actualizar un usuario, es necesario entregar su ID");
        }

        usuarioGateway.buscarPorId(usuario.getIdUsuario());

        Usuario usuarioValidado = validacionesUsuario(usuario);

        return usuarioGateway.actualizarUsuario(usuarioValidado);

    }

    public void eliminarUsuario(String idUsuario){

        if (idUsuario == null || idUsuario.trim().isEmpty() ) {
            throw new BusinessRuleException("Para eliminar un usuario se requiere un id que no sea nulo");
        }

        Usuario usuarioAEliminar = usuarioGateway.buscarPorId(idUsuario);

        if (usuarioAEliminar == null){
            throw new UserNotFoundException("No se puede eliminar. Usuario no encontrado.");
        }

        usuarioGateway.eliminarUsuario(idUsuario);

    }

    private Usuario validacionesUsuario(Usuario usuarioAValidar){
        // Validación menor de edad
        if (usuarioAValidar.getEdad() == null) {
            throw new BusinessRuleException("La edad no puede ser nula");
        } else if (usuarioAValidar.getEdad() < 18) {
            throw new BusinessRuleException("Es menor de edad");
        }

        // Validación del resto de campos
        boolean hayCamposIncompletos = Stream.of(
                usuarioAValidar.getNombre(),
                usuarioAValidar.getEmail(),
                usuarioAValidar.getPass(),
                usuarioAValidar.getRole(),
                usuarioAValidar.getNumeroTelefono()
        ).anyMatch(valor -> valor == null || valor.trim().isEmpty());

        if (hayCamposIncompletos) {
            throw new BusinessRuleException("Todos los campos deben estar completos.");
        }

        // Validación Longitud Pass e email
        if (usuarioAValidar.getPass().length() > 12) {
            throw new BusinessRuleException("Contraseña excede tamaño permitido");
        } else if (usuarioAValidar.getEmail().length() > 30) {
            throw new BusinessRuleException("Email excede tamaño permitido");
        }

        return usuarioAValidar;
    }

}
