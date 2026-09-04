package tp.controllers.usuario;

import io.javalin.http.Context;
import tp.models.entities.comunidad.Persona;
import tp.models.entities.comunidad.Usuario;
import tp.models.repositories.RepositorioPersonas;
import tp.models.repositories.RepositorioUsuarios;

import java.util.HashMap;
import java.util.Map;

public class LoginController {

    public void iniciarSesion(io.javalin.http.Context context) {
        context.render("login.hbs");
    }

    public Usuario existeUsuario(String username, String password){
        Usuario ret = null;

        Usuario usuario = RepositorioUsuarios.getInstancia().findByNombre(username);
        if(usuario != null && usuario.getContrasenia().equals(password)){
            ret = usuario;
        }
        return ret;
    }

    public void verificarUsuario(Context context){
        String username = context.formParam("nombreUsuario");
        String password = context.formParam("password");
        Usuario usuario = existeUsuario(username, password);
        if(usuario != null) {
            context.sessionAttribute("Usuario", usuario);
            context.sessionAttribute("Persona", RepositorioPersonas.getInstancia().findByUsuario(usuario.getId()));
            Persona persona = RepositorioPersonas.getInstancia().findByUsuario(usuario.getId());
            context.sessionAttribute("tipo_rol", persona.getRolPersona());
            switch (persona.getRolPersona()) {
                case BASICO -> context.redirect("/inicio");
                case ADMIN -> context.redirect("/inicio");
                case RESPONSABLE -> context.redirect("/inicio/responsable");
                default -> context.render("405.hbs");
            }
        }
        else {
            Map<String, Object> model = new HashMap<>();
            model.put("mensajeError", "Tu usuario o contraseña no son correctas, compruébalas.");

            context.render("login.hbs", model);
        }
    }

    public void home(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("persona", context.sessionAttribute("Persona"));
        context.render("inicio/home.hbs", model);
    }

    public void inicioAdmin(Context context) {
        context.render("inicio/homeAdmin.hbs");
    }
}
