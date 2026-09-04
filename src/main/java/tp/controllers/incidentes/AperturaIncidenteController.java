package tp.controllers.incidentes;

import io.javalin.http.Context;
import tp.models.entities.comunidad.Comunidad;
import tp.models.entities.comunidad.Miembro;
import tp.models.entities.comunidad.Persona;
import tp.models.entities.entidad.Establecimiento;
import tp.models.entities.servicios.Incidente;
import tp.models.entities.servicios.Servicio;
import tp.models.repositories.RepositorioComunidades;
import tp.models.repositories.RepositorioEstablecimientos;
import tp.models.repositories.RepositorioIncidentes;
import tp.models.repositories.RepositorioServicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AperturaIncidenteController {
    public void create(Context context) { // Vista para dar de alta incidente
        context.render("incidentes/incidentes.hbs");
    }

    public void save(Context context) { // Guardar establecimiento
        String idEstablecimiento = context.formParam("establecimientos");
        String idComunidad = context.formParam("comunidades");
        Persona persona = context.sessionAttribute("Persona");
        context.sessionAttribute("Miembro", persona.getMembresias().stream().filter(miembro ->
                miembro.getComunidad().getId().equals(Long.parseLong(context.formParam("comunidades")))).toList().get(0));


        Comunidad comunidad = RepositorioComunidades.getInstancia().findById(Long.parseLong(idComunidad));
        Establecimiento establecimiento = RepositorioEstablecimientos.getInstancia().findById(Long.parseLong(idEstablecimiento));

        context.sessionAttribute("Establecimiento",establecimiento);
        context.sessionAttribute("Comunidad",comunidad);

        context.redirect("/incidentes/crear/" + establecimiento.getId());
    }

    public void crearIncidente(Context context){
        Map<String, Object> modelServicios = new HashMap<>();
        List<Servicio> serviciosAMostrar = this.getServicios(context);

        //

        Establecimiento establecimiento = context.sessionAttribute("Establecimiento");
        Comunidad comunidad = context.sessionAttribute("Comunidad");

        modelServicios.put("servicios",serviciosAMostrar);
        modelServicios.put("establecimiento",establecimiento);
        modelServicios.put("comunidad",comunidad);
        context.render("incidentes/crearIncidente.hbs",modelServicios);
    }

    public void finalizarCarga(Context context){
        String nombreServicio = context.formParam("servicio");
        String descripcion = context.formParam("descripcion");
        Miembro miembro = context.sessionAttribute("Miembro");
        Servicio servicio = RepositorioServicios.getInstancia().findByNombre(nombreServicio);

        Incidente incidente = miembro.abrirIncidente(servicio, descripcion);
        RepositorioIncidentes.getInstancia().registrar(incidente);
        context.redirect("/incidentes/crear");
    }

    public Map<String, Object> cargarEstablecimientos(){
        Map<String, Object> modelEstablecimientos = new HashMap<>();
        List<Establecimiento> establecimientosAMostrar = RepositorioEstablecimientos.getInstancia().all();
        modelEstablecimientos.put("establecimientos", establecimientosAMostrar);
        return modelEstablecimientos;
    }

    public List<Servicio> getServicios(Context context){
        Establecimiento establecimiento = (Establecimiento) RepositorioEstablecimientos.getInstancia().findById(Long.parseLong(context.pathParam("id")));
        List<Servicio> serviciosDelEstablecimiento = new ArrayList<>();
        serviciosDelEstablecimiento = establecimiento.getServicios();
        return serviciosDelEstablecimiento;
    }
}
