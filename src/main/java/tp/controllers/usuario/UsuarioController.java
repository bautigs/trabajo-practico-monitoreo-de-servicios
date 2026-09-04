package tp.controllers.usuario;

import io.javalin.http.Context;
import tp.models.entities.builders.MiembroBuilder;
import tp.models.entities.builders.PersonaBuilder;
import tp.models.entities.builders.UsuarioBuilder;
import tp.models.entities.comunidad.Miembro;
import tp.models.entities.comunidad.Persona;
import tp.models.entities.comunidad.RolPersona;
import tp.models.entities.comunidad.Usuario;
import tp.models.repositories.RepositorioPersonas;
import tp.models.repositories.RepositorioUsuarios;
import tp.server.utils.ICrudViewsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioController implements ICrudViewsHandler {


  private Usuario usuario = new UsuarioBuilder().build();
  private Miembro miembro = new MiembroBuilder().build();
  private Persona persona = new PersonaBuilder().build();

  @Override
  public void index(Context context) {
    context.render("pantallaInicio.hbs");
  }


  @Override
  public void create(Context context){
  }

  @Override
  public void save(Context context) {

  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void edit(Context context) {

  }

  @Override
  public void update(Context context) {

  }

  @Override
  public void delete(Context context) {

  }

  public void cargarPersonas(Context context){

    Map<String, Object> modelUsuarios = new HashMap<>();
    List<Persona> personas = RepositorioPersonas.getInstancia().all();
    modelUsuarios.put("personas", personas);
    Persona persona = context.sessionAttribute("Persona");
    modelUsuarios.put("persona",persona);
    context.render("listadoRoles.hbs", modelUsuarios);
  }

  public boolean existeUsuario(Context context){
    return RepositorioUsuarios.getInstancia().findByNombre(context.pathParam("nombreUsuario")) == null;
  }



  public void exist(Context context){
    String nombreUsuario = context.pathParam("nombreUsuario");
    List<Usuario> usuarios = RepositorioUsuarios.getInstancia().all();
    Boolean existe = usuarios.stream().anyMatch(usuario1 -> usuario1.getNombreUsuario().equals(nombreUsuario));

    Map<String, Boolean> response = new HashMap<>();
    response.put("exists", existe);

    context.json(response);
  }


  public void getUsuarios(Context context){
    List<Persona> personas = RepositorioPersonas.getInstancia().all();

    List<Map<String, Object>> usuarioData = new ArrayList<>();
    for (Persona persona : personas) {
      Map<String, Object> usuarioMap = new HashMap<>();
      usuarioMap.put("idUsuario", persona.getUsuario().getId());
      usuarioMap.put("idPersona", persona.getId());
      usuarioMap.put("username", persona.getUsuario().getNombreUsuario());
      usuarioMap.put("contrasenia", persona.getUsuario().getContrasenia());
      usuarioMap.put("Rol", persona.getRolPersona());
      usuarioData.add(usuarioMap);
    }
    context.json(usuarioData);
  }

  public void rolAdmin(Context context){
    String idPersona = context.pathParam("idPersona");
    Persona persona = RepositorioPersonas.getInstancia().findById(Long.parseLong(idPersona));
    if (persona.getRolPersona() != RolPersona.ADMIN)
      persona.setRolPersona(RolPersona.ADMIN);
    RepositorioPersonas.getInstancia().update(persona);
  }

  public void cerrarSesion(Context context){
    context.req().getSession().invalidate();
    context.render("pantallaInicio.hbs");
  }


}