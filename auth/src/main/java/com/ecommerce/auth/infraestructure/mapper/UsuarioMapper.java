package com.ecommerce.auth.infraestructure.mapper;


import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.infraestructure.driver_adapters.UsuarioDTO;
import com.ecommerce.auth.infraestructure.driver_adapters.UsuarioData;
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

    // Métodos para DTO
    // De la Web (Record DTO) al Dominio (Modelo)
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

    // Del Dominio (Modelo) a la Web (Record DTO)
    public UsuarioDTO usuarioToDto(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                // usuario.getPass(),
                null, // Dejo el pass null como una prueba para no pasar la clave con el objeto de respuesta
                usuario.getRole(),
                usuario.getEdad(),
                usuario.getNumeroTelefono()
        );
    }
}
