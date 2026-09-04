package tp.controllers.usuario;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import tp.models.entities.builders.MiembroBuilder;
import tp.models.entities.builders.PersonaBuilder;
import tp.models.entities.builders.UsuarioBuilder;
import tp.models.entities.comunidad.*;
import tp.models.entities.entidad.Entidad;
import tp.models.entities.misc.ExcepcionDefinidaPorUsuario;
import tp.models.entities.notificador.NoExisteFormatoException;
import tp.models.entities.services.georef.Localidad;
import tp.models.entities.services.georef.Municipio;
import tp.models.entities.services.georef.Provincia;
import tp.models.entities.servicios.TipoServicio;
import tp.models.entities.validador.Validador;
import tp.models.repositories.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioNuevoController {

    private Usuario usuario = new UsuarioBuilder().build();
    private Miembro miembro = new MiembroBuilder().build();
    private Persona persona = new PersonaBuilder().build();

    public void create(Context context) {
        context.render("crearCuenta.hbs");
    }

    public void crearCuenta(Context context) {
        String nombreApellido = context.formParam("nombreUsuario");
        context.sessionAttribute("nombreApellido", nombreApellido);
        String mail = context.formParam("mailUsuario");
        context.sessionAttribute("mailUsuario",mail);
        String telefono = context.formParam("numero");
        context.sessionAttribute("telefono", telefono);
        context.redirect("/create/carga-de-datos");
    }

    public void cargarDatos(Context context) throws IOException {

        Map<String, Object> modelServicios = this.cargarServicios(context);
        Map<String, Object> modelEntidades = this.cargarEntidades(context);
        Map<String, Object> combinedModel = new HashMap<>();
        combinedModel.putAll(modelServicios);
        combinedModel.putAll(modelEntidades);

        context.render("cargaDeDatos.hbs", combinedModel);
    }

    public void procesarDatos(Context context) {
        try {

            List<TipoServicio> servicios = this.obtenerServicios(context);
            System.out.println(servicios.get(0).getNombre());
            // this.persona.setTiposDeServiciosDeInteres(servicios);

            List<Entidad> entidades = this.obtenerEntidades(context);
            // this.persona.setEntidadesDeInteres(entidades);

            String estrategiaDeNotificacion = context.formParam("estrategia");

            // this.persona.setEstrategia(estrategiaDeNotificacion);
            Persona persona1 = new PersonaBuilder().tipoServicios(servicios).entidades(entidades).build();
            persona1.setEstrategia(estrategiaDeNotificacion);

            if (context.formParam("localidades") != null) {
                Localidad localidad = RepositorioLocalidades.getInstancia().findById(Long.parseLong(context.formParam("localidades")));
                // this.persona.setLocalidad(localidad);
                persona1.setLocalidad(localidad);
            }
            if (context.formParam("municipios") != null) {
                Municipio municipio = RepositorioMunicipios.getInstancia().findById(Long.parseLong(context.formParam("municipios")));
                // this.persona.setMunicipio(municipio);
                persona1.setMunicipio(municipio);
            }

            Provincia provincia = RepositorioProvincias.getInstancia().findById(Long.parseLong(context.formParam("provincias")));
/*
            this.persona.setProvincia(provincia);
            this.persona.setNombreApellido(context.sessionAttribute("nombreApellido"));
            this.persona.setMailDeContacto(context.sessionAttribute("mailUsuario"));
            this.persona.setNumeroDeContacto(context.sessionAttribute("telefono"));
*/

            persona1.setProvincia(provincia);
            persona1.setNombreApellido(context.sessionAttribute("nombreApellido"));
            persona1.setMailDeContacto(context.sessionAttribute("mailUsuario"));
            persona1.setNumeroDeContacto(context.sessionAttribute("telefono"));
            context.sessionAttribute("Persona", persona1);

            context.redirect("/create/carga-de-datos/finalizar");

        } catch (NoExisteFormatoException e) {
            e.printStackTrace();
            context.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Error interno del servidor");
        }
    }

    public void cargaDePassword(Context context) {
        context.render("cargaDePwd.hbs");
    }
    // controller llama al validador


    public boolean esContraValida(Context context) throws ExcepcionDefinidaPorUsuario {
        String password = context.formParam("password");
        return Validador.getInstancia().esContraValida(password);
    }

    public void esContraValidaGET(Context context) throws ExcepcionDefinidaPorUsuario {
        String password = context.pathParam("contrasenia");

        boolean valida = Validador.getInstancia().esContraValida(password);
        String validez = null;

        if(valida == true){
            validez = "esValida";
        }
        else{
            validez = "noEsValida";
        }

        Map<String, String> response = new HashMap<>();
        response.put("validez", validez);

        context.json(response);
    }

    public void finalizarCarga(Context context) throws ExcepcionDefinidaPorUsuario {
        String username = context.formParam("username");
        String password = context.formParam("password");

        if (Validador.getInstancia().esContraValida(password)) {
            Usuario usuario1 = new Usuario();
            usuario1.setNombreUsuario(username);
            usuario1.setContrasenia(password);
            usuario1.setFechaDeAlta(LocalDate.now());
            this.saveUsuario(usuario1);
            Persona persona1 = context.sessionAttribute("Persona");
            persona1.setUsuario(usuario1);
            persona1.setRolPersona(RolPersona.BASICO);
            persona1.setConfiguracion(new Sincronico());
            context.sessionAttribute("Usuario", usuario1);
            context.sessionAttribute("tipo_rol",RolPersona.BASICO);
            this.savePersona(persona1);
            context.redirect("/inicio");
        }
        else {
            Map<String, Object> model = new HashMap<>();
            model.put("mensajeError", "La contraseña ingresada no cumple con los requisitos");

            context.render("cargaDePwd.hbs", model);
        }


    }


    public Map<String, Object> cargarServicios(Context context) {
        Map<String, Object> modelServicios = new HashMap<>();
        List<TipoServicio> serviciosAMostrar = RepositorioTiposDeServicio.getInstancia().all();
        modelServicios.put("servicios", serviciosAMostrar);
        return modelServicios;
    }

    public Map<String, Object> cargarEntidades(Context context) {
        Map<String, Object> modelEntidades = new HashMap<>();
        List<Entidad> entidadesAMostrar = RepositorioEntidades.getInstancia().all();
        modelEntidades.put("entidades", entidadesAMostrar);
        return modelEntidades;
    }

    public void savePersona(Persona persona){
        RepositorioPersonas.getInstancia().registrar(persona);
    }

    public void saveUsuario(Usuario usuario) {
        RepositorioUsuarios.getInstancia().registrar(usuario);
    }

    private List<TipoServicio> obtenerServicios(Context context){
        List<String> servicios = context.formParams("servicios");
        List<TipoServicio> serviciosDeInteres = new ArrayList<>();
        for(String servicio: servicios){
            TipoServicio servicioSeleccionado = RepositorioTiposDeServicio.getInstancia().findByNombre(servicio);
            serviciosDeInteres.add(servicioSeleccionado);
        }
        return serviciosDeInteres;
    }

    private List<Entidad> obtenerEntidades(Context context){
        List<String> entidades = context.formParams("entidades");
        List<Entidad> entidadesDeInteres = new ArrayList<>();
        for(String entidad: entidades){
            Entidad entidadSeleccionada = RepositorioEntidades.getInstancia().findByNombre(entidad);
            entidadesDeInteres.add(entidadSeleccionada);
        }
        return entidadesDeInteres;
    }
}
