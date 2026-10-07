package com.ecommerce.auth.domain.usecase;

import com.ecommerce.auth.domain.exception.RecursoNoEncontradoException;
import com.ecommerce.auth.domain.exception.ReglaNegocioException;
import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.EncrypterGateway;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class UsuarioUseCase {

    // Se implementa la lógica de negocio de las API
    private final UsuarioGateway usuarioGateway;
    private final EncrypterGateway encrypterGateway;

    // Caso de uso guardar usuario

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new ReglaNegocioException("El usuario recibido no puede ser nulo en todos su campos");
        }

        Usuario usuarioValidado = validacionesUsuario(usuario, null); // Falta validacion de usuarios con mismo correo

        String passEncrypt = encrypterGateway.encrypt(usuarioValidado.getPass());
        usuarioValidado.setPass(passEncrypt);

        return usuarioGateway.guardarUsuario(usuarioValidado);
    }

    public Usuario buscarUsuarioPorId(String idUsuario){

        if (idUsuario == null || idUsuario.trim().isEmpty() ) {
            throw new ReglaNegocioException("El id no puede ser nulo");
        }

        Usuario usuarioBuscado = usuarioGateway.buscarPorId(idUsuario);

        if (usuarioBuscado == null){
            throw new RecursoNoEncontradoException("Usuario no encontrado");
        }

        return usuarioBuscado;

    }

    public String login(String email, String pass){

        if (pass == null || email == null) {
            throw new ReglaNegocioException("Las credenciales no pueden ser nulas");
        }

        Usuario usuarioPorAutenticar = usuarioGateway.buscarPorEmail(email);

        if (usuarioPorAutenticar == null) {
            throw new RecursoNoEncontradoException("Credenciales Invalidas");
        }

        if (usuarioPorAutenticar.getEmail() == null || usuarioPorAutenticar.getPass() == null) {
            throw new RecursoNoEncontradoException("Credenciales Invalidas");
        }

        if (Boolean.TRUE.equals(encrypterGateway.checkPass(pass, usuarioPorAutenticar.getPass()))){
            return "Autenticado como " + usuarioPorAutenticar.getNombre();
        } else {
            return "Credenciales Invalidas";
        }

    }

    public Usuario actualizarUsuario(Usuario usuario){
        if (usuario == null) {
            throw new ReglaNegocioException("El usuario recibido no puede ser nulo.");
        }

        if (usuario.getIdUsuario() == null || usuario.getIdUsuario().trim().isEmpty()) {
            throw new ReglaNegocioException("Para actualizar un usuario, es necesario entregar un ID válido");
        }

        Usuario usuarioExistente = usuarioGateway.buscarPorId(usuario.getIdUsuario());
        if (usuarioExistente == null) {
            throw new RecursoNoEncontradoException("No se puede actualizar. Usuario no encontrado.");
        }

        Usuario usuarioValidado = validacionesUsuario(usuario, usuario.getIdUsuario());

        String passEncrypt = encrypterGateway.encrypt(usuarioValidado.getPass());
        usuarioValidado.setPass(passEncrypt);

        return usuarioGateway.actualizarUsuario(usuarioValidado);
    }

    public void eliminarUsuario(String idUsuario){

        if (idUsuario == null || idUsuario.trim().isEmpty() ) {
            throw new ReglaNegocioException("Para eliminar un usuario se requiere un id que no sea nulo");
        }

        Usuario usuarioAEliminar = usuarioGateway.buscarPorId(idUsuario);

        if (usuarioAEliminar == null){
            throw new RecursoNoEncontradoException("No se puede eliminar. Usuario no encontrado.");
        }

        usuarioGateway.eliminarUsuario(idUsuario);

    }

    private Usuario validacionesUsuario(Usuario usuarioAValidar, String idUsuarioActual){
        // Validación menor de edad
        if (usuarioAValidar.getEdad() == null) {
            throw new ReglaNegocioException("La edad no puede ser nula");
        } else if (usuarioAValidar.getEdad() < 18) {
            throw new ReglaNegocioException("Es menor de edad");
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
            throw new ReglaNegocioException("Todos los campos deben estar completos.");
        }

        // Validación Longitud Pass e email
        if (usuarioAValidar.getPass().length() > 20) {
            throw new ReglaNegocioException("Contraseña excede tamaño permitido");
        } else if (usuarioAValidar.getEmail().length() > 30) {
            throw new ReglaNegocioException("Email excede tamaño permitido");
        }

        Usuario usuarioConMismoEmail = usuarioGateway.buscarPorEmail(usuarioAValidar.getEmail());

        if (usuarioConMismoEmail != null && !Objects.equals(usuarioConMismoEmail.getIdUsuario(), idUsuarioActual)) {
            throw new ReglaNegocioException("El correo electrónico ya se encuentra registrado por otro usuario.");
        }


        return usuarioAValidar;
    }

}
