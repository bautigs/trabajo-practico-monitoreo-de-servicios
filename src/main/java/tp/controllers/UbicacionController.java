package tp.controllers;

import io.javalin.http.Context;
import tp.models.entities.services.georef.Localidad;
import tp.models.entities.services.georef.Municipio;
import tp.models.entities.services.georef.Provincia;
import tp.models.repositories.RepositorioLocalidades;
import tp.models.repositories.RepositorioMunicipios;
import tp.models.repositories.RepositorioProvincias;
import tp.server.utils.ICrudViewsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UbicacionController implements ICrudViewsHandler {
    @Override
    public void index(Context context) {
        RepositorioProvincias.getInstancia().all();
    }

    @Override
    public void create(Context context) {

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

    public void provincias(Context context){
        List<Provincia> provincias = RepositorioProvincias.getInstancia().all();

        List<Map<String, Object>> provinciaData = new ArrayList<>();
        for (Provincia provincia : provincias) {
            Map<String, Object> provinciaMap = new HashMap<>();
            provinciaMap.put("id", provincia.getId());
            provinciaMap.put("nombre", provincia.getNombre());
            provinciaData.add(provinciaMap);
        }

        context.json(provinciaData);
    }

    public void municipios(Context context){
        String provinciaId = context.pathParam("idProvincia");
        List<Municipio> municipios = RepositorioMunicipios.getInstancia().findByProvincia(Long.valueOf(provinciaId));
        List<Map<String, Object>> municipioData = new ArrayList<>();
        for (Municipio municipio : municipios) {
            Map<String, Object> municipioMap = new HashMap<>();
            municipioMap.put("id", municipio.getId());
            municipioMap.put("nombre", municipio.getNombre());
            municipioData.add(municipioMap);
        }
        context.json(municipioData);
    }

    public void localidades(Context context){
        String idMunicipio = context.pathParam("idMunicipio");
        List<Localidad> localidades = RepositorioLocalidades.getInstancia().findByMunicipio(Long.valueOf(idMunicipio));
        List<Map<String, Object>> localidadData = new ArrayList<>();
        for (Localidad localidad : localidades) {
            Map<String, Object> localidadMap = new HashMap<>();
            localidadMap.put("id", localidad.getId());
            localidadMap.put("nombre", localidad.getNombre());
            localidadData.add(localidadMap);
        }
        context.json(localidadData);
    }

}
