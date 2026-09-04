package tp.controllers.incidentes;

import io.javalin.http.Context;
import tp.controllers.incidentes.model.IncidenteFiltradoModel;
import tp.controllers.incidentes.model.ServicioFiltradoModel;
import tp.models.entities.comunidad.Comunidad;
import tp.models.entities.comunidad.Persona;
import tp.models.entities.entidad.Establecimiento;
import tp.models.entities.servicios.Servicio;
import tp.models.repositories.RepositorioComunidades;
import tp.models.repositories.RepositorioEstablecimientos;
import tp.server.utils.ICrudViewsHandler;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncidentesController implements ICrudViewsHandler {


    @Override
    public void index(Context context) { // Todos los incidentes

    }

    @Override
    public void create(Context context) { // Vista para dar de alta incidente
    }

    @Override
    public void save(Context context) { // Guardar establecimiento
    }


    @Override
    public void show(Context context) { // Mostrar detalle de un incidente

    }

    @Override
    public void edit(Context context) { // Es necesario?

    }

    @Override
    public void update(Context context) { // Actualizar el estado de un incidente

    }

    @Override
    public void delete(Context context) {

    }

    /*
      public void crearListado(Context context){
        Map<String, Object> modelEstablecimientos = this.cargarEstablecimientos();
        context.render("incidentes/listadoIncidentes.hbs", modelEstablecimientos);
      }
     */
    public void crearListado(Context context) {
        //Map<String, Object> modelEstablecimientos = this.cargarEstablecimientos();
        Persona persona = context.sessionAttribute("Persona");
        Map<String,Persona> model = new HashMap<>();
        model.put("persona",persona);
        context.render("incidentes/listadoIncidentes.hbs",model);
    }

    public void crearListadoSave(Context context) {
        String idEstablecimiento = context.formParam("establecimientos");
        String id = context.formParam("comunidades");
        context.sessionAttribute("idComunidad", id);
        Establecimiento establecimiento = RepositorioEstablecimientos.getInstancia().findById(Long.parseLong(idEstablecimiento));
        context.redirect("/incidente/listado/" + establecimiento.getId());
    }

    public void listarIncidentes(Context context) {
        Map<String, Object> modelIncidentes = new HashMap<>();
        String idComunidad = context.sessionAttribute("idComunidad");
        Comunidad comunidad = RepositorioComunidades.getInstancia().findById(Long.parseLong(idComunidad));
        Establecimiento establecimiento = RepositorioEstablecimientos.getInstancia().findById(Long.parseLong(context.pathParam("id")));
        List<Servicio> servicios = establecimiento.obtenerServiciosConIncidente();
        Persona persona = context.sessionAttribute("Persona");


        // Filtrar los incidentes para cada servicio
        List<ServicioFiltradoModel> servicioFiltrado = new ArrayList<>();
        servicios.forEach(servicio ->
                servicioFiltrado.add(ServicioFiltradoModel.builder()
                        .name(servicio.getNombre())
                        .tipoServicio(servicio.getTipoServicio().getNombre())
                        .incidentes(servicio.incidentesDeComunidad(Long.valueOf(idComunidad)).stream()
                                .map(incidente -> IncidenteFiltradoModel
                                        .builder()
                                        .fechaApertura(incidente.getFechaApertura().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                                        .descripcion(incidente.getDescripcion())
                                        .estado(incidente.getEstado())
                                        .build()).toList())
                        .build()));
        modelIncidentes.put("establecimiento", establecimiento);
        modelIncidentes.put("serviciosFiltrado", servicioFiltrado);
        modelIncidentes.put("comunidad", comunidad);
        modelIncidentes.put("persona",persona);
        context.render("incidentes/filtradoIncidentes.hbs", modelIncidentes);
    }
}

