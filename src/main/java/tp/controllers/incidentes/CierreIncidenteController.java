package tp.controllers.incidentes;

import io.javalin.http.Context;
import tp.models.entities.comunidad.Comunidad;
import tp.models.entities.comunidad.Miembro;
import tp.models.entities.comunidad.Persona;
import tp.models.entities.entidad.Establecimiento;
import tp.models.entities.servicios.Incidente;
import tp.models.repositories.RepositorioComunidades;
import tp.models.repositories.RepositorioEstablecimientos;
import tp.models.repositories.RepositorioIncidentes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CierreIncidenteController {
    public void cerrar(Context context){
        Map<String, Object> modelEstablecimientos = this.cargarEstablecimientosConIncidentes();



        context.render("incidentes/cerrarIncidente.hbs", modelEstablecimientos);
    }


    // recibo el establecimiento que quiero filtrar
    public void procesarCierre(Context context){
        String nombreEstablecimiento = context.formParam("establecimientos");
        context.sessionAttribute("idComunidad", context.formParam("comunidades"));
        Persona persona = context.sessionAttribute("Persona");

        context.sessionAttribute("Miembro", persona.getMembresias().stream().filter(miembro ->
                miembro.getComunidad().getId().equals(Long.parseLong(context.formParam("comunidades")))).toList().get(0));
        Establecimiento establecimiento = RepositorioEstablecimientos.getInstancia().findById(Long.parseLong(nombreEstablecimiento));
        Comunidad comunidad = RepositorioComunidades.getInstancia().findById(Long.parseLong(context.formParam("comunidades")));
        context.sessionAttribute("Comunidad",comunidad);
        context.sessionAttribute("Establecimiento", establecimiento);
        context.redirect("/incidentes/cerrar/" + establecimiento.getId());
    }

    // aca se pone en el map
    public Map<String, Object> cargarIncidentes(Context context){
        Comunidad comunidad = context.sessionAttribute("Comunidad");
        Establecimiento establecimiento = context.sessionAttribute("Establecimiento");
        Map<String, Object> modelIncidentes = new HashMap<>();
        List<Incidente> incidentesAMostrar = this.getIncidentes(context);
        modelIncidentes.put("incidentes",incidentesAMostrar);
        modelIncidentes.put("establecimiento", establecimiento);
        modelIncidentes.put("comunidad", comunidad);
        context.render("incidentes/finalizarCierre.hbs", modelIncidentes);
        return modelIncidentes;
    }

    public List<Incidente> getIncidentes(Context context){
        Establecimiento establecimiento = RepositorioEstablecimientos.getInstancia().findById(Long.parseLong(context.pathParam("id")));
        return establecimiento.obtenerIncidentesDelEstablecimiento()
                .stream().filter(incidente ->
                        incidente.getComunidad().getId().equals(Long.parseLong((context.sessionAttribute("idComunidad"))))).toList();
    }

    public void finalizarCierre(Context context){
        String incidente = context.formParam("incidente");
        String idDelIncidente = this.parsearIncidente(incidente);
        String descripcion = context.formParam("descripcion");
        Miembro miembro = context.sessionAttribute("Miembro");

        Incidente incidenteACerrar = RepositorioIncidentes.getInstancia().findById(Long.parseLong(idDelIncidente));
        miembro.cierreDeIncidente(incidenteACerrar);
        RepositorioIncidentes.getInstancia().actualizar(incidenteACerrar);
        context.redirect("/incidentes/cerrar");
    }

    public Map<String, Object> cargarEstablecimientosConIncidentes(){
        Map<String, Object> modelEstablecimientos = new HashMap<>();
        List<Establecimiento> establecimientosAMostrar = RepositorioEstablecimientos.getInstancia().all();

        establecimientosAMostrar = establecimientosAMostrar.stream().filter(Establecimiento::esEstablecimientoConIncidentes).toList();

        modelEstablecimientos.put("establecimientos", establecimientosAMostrar);
        return modelEstablecimientos;
    }

    public String parsearIncidente(String incidente){
        String[ ] aux = incidente.split(" ");
        return aux[0];
    }
//Para que se necesita?
    public String obtenerDescripcion(String incidente){
        String[ ] aux = incidente.split(" ");
        return aux[1];
    }
}
