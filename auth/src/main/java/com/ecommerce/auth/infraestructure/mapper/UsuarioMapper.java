package com.ecommerce.auth.infraestructure.mapper;


import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.infraestructure.driver_adapters.base_datos.UsuarioData;
import com.ecommerce.auth.infraestructure.entry_points.dto.UsuarioDTO;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toUsuario (UsuarioData usuarioData) {
        return new Usuario(
                usuarioData.getIdUsuario(),
                usuarioData.getNombre(),
                usuarioData.getEmail(),
                usuarioData.getPass(),
                usuarioData.getRole(),
                usuarioData.getEdad(),
                usuarioData.getNumeroTelefono()
        );
    }

    public UsuarioData toUsuarioData (Usuario usuario) {
        return new UsuarioData(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPass(),
                usuario.getRole(),
                usuario.getEdad(),
                usuario.getNumeroTelefono()
        );
    }

    // De la Web al Dominio
    public Usuario dtoToUsuario(UsuarioDTO dto) {
        return new Usuario(
                dto.idUsuario(),
                dto.nombre(),
                dto.email(),
                dto.pass(),
                dto.role(),
                dto.edad(),
                dto.numeroTelefono()
        );
    }

    // Del Dominio a la Web
    public UsuarioDTO usuarioToDto(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                // usuario.getPass(),
                null, // Dejo el pass null como una prueba, no se deberia pasar la clave aunque este encriptada
                usuario.getRole(),
                usuario.getEdad(),
                usuario.getNumeroTelefono()
        );
    }

}
