package tp.controllers.servicios;

import io.javalin.http.Context;
import tp.models.entities.servicios.TipoServicio;
import tp.models.repositories.RepositorioTiposDeServicio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioController {
    public void crearTipoServicio(Context context){
        String nombre = context.queryParam("nombre");
        TipoServicio tipoServicio = new TipoServicio();
        tipoServicio.setNombre(nombre);
        RepositorioTiposDeServicio.getInstancia().registrar(tipoServicio);
    }

    public void getTiposDeServicio(Context context){
        List<TipoServicio> tipos = RepositorioTiposDeServicio.getInstancia().all();

        List<Map<String, Object>> utipoEntidadData = new ArrayList<>();
        for (TipoServicio tipoServicio : tipos) {
            Map<String, Object> tipoMap = new HashMap<>();
            tipoMap.put("id", tipoServicio.getId());
            tipoMap.put("nombre", tipoServicio.getNombre());
            utipoEntidadData.add(tipoMap);
        }
        context.json(utipoEntidadData);
    }
}
