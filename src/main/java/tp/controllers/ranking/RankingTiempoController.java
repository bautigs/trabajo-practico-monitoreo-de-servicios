package tp.controllers.ranking;

import io.javalin.http.Context;
import tp.models.entities.comunidad.Persona;
import tp.models.entities.entidad.Entidad;
import tp.models.entities.ranking.CriterioTiempoDeCierre;
import tp.models.repositories.RepositorioEntidades;
import tp.server.utils.ICrudViewsHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingTiempoController implements ICrudViewsHandler {
    @Override
    public void index(Context context) {

    }

    public void cargarRankingTiempo(Context context) {
        Map<String, Object> modelRankingTiempo = this.cargarModelTiempo(context);
        context.render("ranking/rankingTiempo.hbs", modelRankingTiempo);
    }

    public Map<String, Object> cargarModelTiempo(Context context){
        CriterioTiempoDeCierre criterioCantidadIncidentes = new CriterioTiempoDeCierre();
        Map<String, Object> modelRankingTiempo = new HashMap<>();
        List<Entidad> entidadades = RepositorioEntidades.getInstancia().all();
        criterioCantidadIncidentes.generarReporte(entidadades);
        List<Entidad> entidadesAMostrar = criterioCantidadIncidentes.getUltimaListaCriterioTiempo();
        modelRankingTiempo.put("entidades", entidadesAMostrar);
        Persona persona = context.sessionAttribute("Persona");
        modelRankingTiempo.put("persona",persona);
        return modelRankingTiempo;
    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) throws IOException {

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
}