package tp.controllers.entidad;

import io.javalin.http.Context;
import tp.models.entities.entidad.Entidad;
import tp.models.entities.entidad.Establecimiento;
import tp.models.entities.entidad.TipoEntidad;
import tp.models.repositories.RepositorioEntidades;
import tp.models.repositories.RepositorioTiposDeEntidad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntidadController {

    public void crearTipoEntidad(Context context){
        String nombre = context.queryParam("nombre");
        TipoEntidad tipoEntidad = new TipoEntidad();
        tipoEntidad.setNombre(nombre);
        RepositorioTiposDeEntidad.getInstancia().registrar(tipoEntidad);
    }

    public void getTiposDeEntidad(Context context){
        List<TipoEntidad> tipos = RepositorioTiposDeEntidad.getInstancia().all();

        List<Map<String, Object>> utipoEntidadData = new ArrayList<>();
        for (TipoEntidad tipoEntidad : tipos) {
            Map<String, Object> tipoMap = new HashMap<>();
            tipoMap.put("id", tipoEntidad.getId());
            tipoMap.put("nombre", tipoEntidad.getNombre());
            utipoEntidadData.add(tipoMap);
        }
        context.json(utipoEntidadData);
    }

    public void crearEntidad(Context context){
        String nombre = context.queryParam("nombre");
        String tipoEntidadId = context.queryParam("tipo");

        TipoEntidad tipoEntidad = RepositorioTiposDeEntidad.getInstancia()
                .findById(Long.parseLong(tipoEntidadId));
        if (tipoEntidad != null) {
            Entidad entidad = new Entidad();
            entidad.setTipoEntidad(tipoEntidad);
            entidad.setNombre(nombre);
            RepositorioEntidades.getInstancia().registrar(entidad);
        }
    }

    public void getEntidad(Context context) {
        List<Entidad> entidades = RepositorioEntidades.getInstancia().all();

        List<Map<String, Object>> entidadData = new ArrayList<>();
        for (Entidad entidad : entidades) {
            Map<String, Object> entidadMap = new HashMap<>();
            entidadMap.put("id", entidad.getId());
            entidadMap.put("nombre", entidad.getNombre());

            List<Map<String, Object>> establecimientoData = new ArrayList<>();
            for (Establecimiento establecimiento : entidad.getListaEstablecimientos()) {
                Map<String, Object> establecimientoMap = new HashMap<>();
                establecimientoMap.put("idEstablecimiento", establecimiento.getId());
                establecimientoMap.put("nombreEstablecimiento", establecimiento.getNombre());

                establecimientoData.add(establecimientoMap);
            }

            entidadMap.put("establecimientos", establecimientoData);

            entidadData.add(entidadMap);
        }

        context.json(entidadData);
    }

}
