package com.ecommerce.auth.domain.usecase;

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
            throw new IllegalArgumentException("El objeto usuario no puede ser nulo.");
        }

        // Validación menor de edad
        if (usuario.getEdad() == null) {
            throw new IllegalArgumentException("La edad no puede ser nula");
        } else if (usuario.getEdad() < 18) {
            throw new RuntimeException("Es menor de edad");
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
            throw new IllegalArgumentException("Todos los campos deben estar completos.");
        }

        // Validación Longitud Pass e email
        if (usuario.getPass().length() > 12) {
            throw new IllegalArgumentException("Contraseña excede tamaño permitido");
        } else if (usuario.getEmail().length() > 30) {
            throw new IllegalArgumentException("Email excede tamaño permitido");
        }


        return usuarioGateway.guardarUsuario(usuario);
    }

    public Usuario buscarUsuarioPorId(String idUsuario){

        if (idUsuario == null || idUsuario.trim().isEmpty() ) {
            throw new IllegalArgumentException("Para buscar un usuario por su Id, esta ultimo no puede ser nulo");
        }

        Usuario usuarioBuscado = usuarioGateway.buscarPorId(idUsuario);

        if (usuarioBuscado == null){
            throw new RuntimeException("Usuario no encontrado");
        }

        return usuarioBuscado;

    }

}
