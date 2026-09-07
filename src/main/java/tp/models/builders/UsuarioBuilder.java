package tp.models.builders;

import tp.models.comunidad.Usuario;

import java.time.LocalDate;

public class UsuarioBuilder {
    private Usuario usuario = new Usuario();

    public UsuarioBuilder usuario(String username){
        usuario.setNombreUsuario(username);
        return this;
    }

    public UsuarioBuilder contrasenia(String contra){
        usuario.setContrasenia(contra);
        return this;
    }
    public UsuarioBuilder alta(LocalDate localDate){
        usuario.setFechaDeAlta(localDate);
        return this;
    }

    public Usuario build(){
        return this.usuario;
    }
}
