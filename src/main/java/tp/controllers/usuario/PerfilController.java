package tp.controllers.usuario;

import io.javalin.http.Context;
import tp.models.entities.comunidad.Persona;
import tp.models.entities.notificador.wpp.EstrategiaDeWPP;
import tp.models.entities.services.georef.Localidad;
import tp.models.entities.services.georef.Municipio;
import tp.models.entities.services.georef.Provincia;
import tp.models.repositories.RepositorioLocalidades;
import tp.models.repositories.RepositorioMunicipios;
import tp.models.repositories.RepositorioPersonas;
import tp.models.repositories.RepositorioProvincias;

import java.util.HashMap;
import java.util.Map;

public class PerfilController {

    public void perfil(Context context){
        Persona persona = context.sessionAttribute("Persona");
        Map<String, Persona> model = new HashMap<>();
        model.put("persona", persona);
        if(persona.getEstrategia().getClass().equals(EstrategiaDeWPP.class))
            persona.setEstrategiaString("Whatsapp");
        else
            persona.setEstrategiaString("Mail");

        context.render("inicio/miPerfil.hbs", model);
    }

    public void nuevaUbicacion(Context context){
        Persona persona = context.sessionAttribute("Persona");
        Map<String,Persona> model = new HashMap<>();
        model.put("persona",persona);
        context.render("ubicacion/informarNuevaUbicacion.hbs",model);
    }

    public void setUbicacion(Context context){
        Persona persona = context.sessionAttribute("Persona");

        if (context.formParam("localidades") != null) {
            System.out.println(context.formParam("localidades"));
            Localidad localidad = RepositorioLocalidades.getInstancia().findById(Long.parseLong(context.formParam("localidades")));
            persona.setLocalidad(localidad);
        }
        if (context.formParam("municipios") != null) {
            Municipio municipio = RepositorioMunicipios.getInstancia().findById(Long.parseLong(context.formParam("municipios")));
            persona.setMunicipio(municipio);
        }

        Provincia provincia = RepositorioProvincias.getInstancia().findById(Long.parseLong(context.formParam("provincias")));
        persona.setProvincia(provincia);
        RepositorioPersonas.getInstancia().update(persona);
        context.sessionAttribute("Persona", persona);
        context.redirect("/perfil");
    }
}
