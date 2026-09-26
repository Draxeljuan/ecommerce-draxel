package com.ecommerce.auth.domain.usecase;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class UsuarioUseCase {

    // Se implementa la lógica de negocio de las API
    private final UsuarioGateway usuarioGateway;

    // Caso de uso guardar usuario

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new NullPointerException("El usuario recibido no puede ser nulo en todos su campos");
        }

        Usuario usuarioValidado = validacionesUsuario(usuario);

        return usuarioGateway.guardarUsuario(usuarioValidado);
    }

    public Usuario buscarUsuarioPorId(String idUsuario){

        if (idUsuario == null || idUsuario.trim().isEmpty() ) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }

        Usuario usuarioBuscado = usuarioGateway.buscarPorId(idUsuario);

        if (usuarioBuscado == null){
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        return usuarioBuscado;

    }

    public String login(String email, String pass){

        if (pass == null && email == null) {
            throw new IllegalArgumentException("Las credenciales no pueden ser nulas");
        }

        Usuario usuarioPorAutenticar = usuarioGateway.buscarPorEmail(email);

        if (!Objects.equals(usuarioPorAutenticar.getEmail(), email) || !Objects.equals(usuarioPorAutenticar.getPass(), pass)) {
            throw new IllegalArgumentException("Credenciales Invalidas");
        }

        return "Autenticado como " + usuarioPorAutenticar.getNombre();

    }

    public Usuario actualizarUsuario(Usuario usuario){

        if (usuario == null) {
            throw new IllegalArgumentException("El usuario recibido no puede ser nulo.");
        }

        if (usuario.getIdUsuario() == null) {
            throw new IllegalArgumentException("Para actualizar un usuario, es necesario entregar su ID");
        }

        usuarioGateway.buscarPorId(usuario.getIdUsuario());

        Usuario usuarioValidado = validacionesUsuario(usuario);

        return usuarioGateway.actualizarUsuario(usuarioValidado);

    }

    public void eliminarUsuario(String idUsuario){

        if (idUsuario == null || idUsuario.trim().isEmpty() ) {
            throw new IllegalArgumentException("Para eliminar un usuario se requiere un id que no sea nulo");
        }

        Usuario usuarioAEliminar = usuarioGateway.buscarPorId(idUsuario);

        if (usuarioAEliminar == null){
            throw new IllegalArgumentException("No se puede eliminar. Usuario no encontrado.");
        }

        usuarioGateway.eliminarUsuario(idUsuario);

    }

    private Usuario validacionesUsuario(Usuario usuarioAValidar){
        // Validación menor de edad
        if (usuarioAValidar.getEdad() == null) {
            throw new IllegalArgumentException("La edad no puede ser nula");
        } else if (usuarioAValidar.getEdad() < 18) {
            throw new IllegalArgumentException("Es menor de edad");
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
            throw new IllegalArgumentException("Todos los campos deben estar completos.");
        }

        // Validación Longitud Pass e email
        if (usuarioAValidar.getPass().length() > 12) {
            throw new IllegalArgumentException("Contraseña excede tamaño permitido");
        } else if (usuarioAValidar.getEmail().length() > 30) {
            throw new IllegalArgumentException("Email excede tamaño permitido");
        }

        return usuarioAValidar;
    }

}
