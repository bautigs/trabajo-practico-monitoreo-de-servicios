package tp.controllers.comunidad;

import io.javalin.http.Context;
import tp.models.entities.builders.ComunidadBuilder;
import tp.models.entities.builders.MiembroBuilder;
import tp.models.entities.comunidad.*;
import tp.models.entities.servicios.TipoServicio;
import tp.models.repositories.RepositorioComunidades;
import tp.models.repositories.RepositorioMiembros;
import tp.models.repositories.RepositorioPersonas;
import tp.models.repositories.RepositorioTiposDeServicio;

import java.util.*;

public class ComunidadController {
    public void allUser(Context context) {
        Persona persona = context.sessionAttribute("Persona");

        if (persona.getRolPersona().equals(RolPersona.BASICO)) {
            List<Comunidad> comunidades = RepositorioComunidades.getInstancia().all();

            List<Map<String, Object>> comunidadesData = new ArrayList<>();

            for (Comunidad comunidad : comunidades) {
                if (!persona.getComunidades().stream().filter(c -> Objects.equals(c.getId(), comunidad.getId())).toList().isEmpty()) {
                    Map<String, Object> comunidadesMap = new HashMap<>();
                    comunidadesMap.put("id", comunidad.getId());
                    comunidadesMap.put("nombre", comunidad.getNombre());
                    comunidadesData.add(comunidadesMap);
                }
            }
            context.json(comunidadesData);
        }
        if (persona.getRolPersona().equals(RolPersona.ADMIN)) {
            context.redirect("/comunidades");
        }
    }

    public void all(Context context) {
        List<Comunidad> comunidades = RepositorioComunidades.getInstancia().all();

        List<Map<String, Object>> comunidadesData = new ArrayList<>();
        for (Comunidad comunidad : comunidades) {
            Map<String, Object> comunidadesMap = new HashMap<>();
            comunidadesMap.put("id", comunidad.getId());
            comunidadesMap.put("nombre", comunidad.getNombre());
            comunidadesData.add(comunidadesMap);
        }
        context.json(comunidadesData);
    }

    public void cargarComunidades(Context context) {
        Persona persona = context.sessionAttribute("Persona");
        Map<String, Object> modelPersona = new HashMap<>();
        modelPersona.put("persona", persona);
        context.render("comunidades/misComunidades.hbs", modelPersona);
    }

    public void cargarDatos(Context context) {
        Map<String, Object> model = new HashMap<>();
        List<TipoServicio> list = RepositorioTiposDeServicio.getInstancia().all();
        model.put("servicios", list);
        Persona persona = context.sessionAttribute("Persona");
        model.put("persona", persona);
        context.render("comunidades/crearComunidad.hbs", model);
    }

    public void mostrarComunidades(Context context) {
        List<Comunidad> comunidades = RepositorioComunidades.getInstancia().all();
        Persona persona = context.sessionAttribute("Persona");
        comunidades.stream().filter(comunidad -> persona.tieneComunidades() ||
            !persona.getComunidades().stream().noneMatch(c1 -> c1.getId() == comunidad.getId()));
        Map<String, Object> modelComunidad = new HashMap<>();
        modelComunidad.put("comunidades", comunidades);
        modelComunidad.put("persona", persona);
        context.render("comunidades/unirseComunidad.hbs", modelComunidad);
    }

    public void unirseAComunidad(Context context) {
        String id = context.pathParam("id");
        Persona persona = context.sessionAttribute("Persona");
        Comunidad comunidad = RepositorioComunidades.getInstancia().findById(Long.parseLong(id));

        Miembro miembro = new MiembroBuilder().condicionMiembre(CondicionDeMiembro.AFECTADO).
            rol(Rol.USUARIOBASICO).
            comunidad(comunidad).build();

        miembro.setPersona(persona);
        persona.agregarMembresia(miembro);
        comunidad.agregaMiembro(miembro);

        RepositorioMiembros.getInstancia().registrar(miembro);
        RepositorioPersonas.getInstancia().update(persona);
        RepositorioComunidades.getInstancia().update(comunidad);

        context.redirect("../mis-comunidades");

    }


    public void crearComunidad(Context context) {
        List<TipoServicio> lista = this.obtenerServicios(context);
        String nombre = context.formParam("nombre");
        Persona persona = context.sessionAttribute("Persona");
        Comunidad comunidad = new ComunidadBuilder().nombre(nombre).tipoServicios(lista).build();
        Miembro miembro = new MiembroBuilder()
            .condicionMiembre(CondicionDeMiembro.AFECTADO)
            .rol(Rol.ADMINISTRADOR)
            .comunidad(comunidad).build();

        miembro.setPersona(persona);


        RepositorioComunidades.getInstancia().registrar(comunidad);
        RepositorioMiembros.getInstancia().registrar(miembro);
        comunidad.agregaMiembro(miembro);
        persona.agregarMembresia(miembro);
        RepositorioPersonas.getInstancia().update(persona);
        RepositorioComunidades.getInstancia().update(comunidad);


        context.redirect("../mis-comunidades");
    }


    private List<TipoServicio> obtenerServicios(Context context) {
        List<String> servicios = context.formParams("servicios");
        List<TipoServicio> serviciosDeInteres = new ArrayList<>();
        for (String servicio : servicios) {
            TipoServicio servicioSeleccionado = RepositorioTiposDeServicio.getInstancia().findByNombre(servicio);
            serviciosDeInteres.add(servicioSeleccionado);
        }
        return serviciosDeInteres;
    }

    //Funciona raro modifica si vuelvo a levantar no en momento de ejecucion
    public void cambiarCondicion(Context context) {
        Persona persona = context.sessionAttribute("Persona");
        String id = context.pathParam("id");
        String idComunidad = context.pathParam("idComunidad");
        Comunidad comunidad = RepositorioComunidades.getInstancia().findById(Long.parseLong(idComunidad));
        Miembro miembro = comunidad.getMiembros().stream().filter(miembroCom -> miembroCom.getId().equals(Long.parseLong(id))).findFirst().orElse(null);
        if (miembro.getCondicionDeMiembro().equals(CondicionDeMiembro.AFECTADO)) {
            miembro.setCondicionDeMiembro(CondicionDeMiembro.OBSERVADOR);
            RepositorioPersonas.getInstancia().update(persona);
            RepositorioMiembros.getInstancia().update(miembro);
        } else {
            miembro.setCondicionDeMiembro(CondicionDeMiembro.AFECTADO);
            RepositorioPersonas.getInstancia().update(persona);
            RepositorioMiembros.getInstancia().update(miembro);
        }
        RepositorioPersonas.getInstancia().update(persona);
        RepositorioMiembros.getInstancia().update(miembro);
        context.redirect("/mis-comunidades");
    }

    public void eliminarseDeComunidad(Context context) {
        String idMiembro = context.pathParam("id");
        System.out.println(idMiembro);
        String idComunidad = context.pathParam("idComunidad");
        System.out.println(idComunidad);
        Persona persona = context.sessionAttribute("Persona");

        Comunidad comunidad = RepositorioComunidades.getInstancia().findById(Long.parseLong(idComunidad));
        // Miembro miembro = comunidad.getMiembros().stream().filter(miembroCom -> miembroCom.getId().equals(Long.parseLong(id))).findFirst().orElse(null);
        Miembro miembro = RepositorioMiembros.getInstancia().findById(Long.parseLong(idMiembro));

        persona.eliminarMembresia(miembro);
        comunidad.eliminarMiembro(miembro);
        miembro.setPersona(null);

        RepositorioComunidades.getInstancia().update(comunidad);
        RepositorioPersonas.getInstancia().update(persona);
        RepositorioPersonas.getInstancia().refresh(persona);
        RepositorioMiembros.getInstancia().update(miembro);

        this.cargarComunidadesPosterior(context);
       /* context.redirect("/mis-comunidades");*/
    }

    public void cargarComunidadesPosterior(Context context) {
        Persona persona = context.sessionAttribute("Persona");
        Map<String, Object> modelPersona = new HashMap<>();
        Persona personaAct = RepositorioPersonas.getInstancia().findById(persona.getId());
        modelPersona.put("persona", personaAct);
        context.render("comunidades/misComunidades.hbs", modelPersona);
    }
}
