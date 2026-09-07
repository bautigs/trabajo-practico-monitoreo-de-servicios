package tp.models.notificador;

import tp.models.comunidad.Miembro;
import tp.models.entidad.Establecimiento;

import java.util.List;

public class AdministracionDeSugerencias {

    private static AdministracionDeSugerencias instancia = null;

    public static AdministracionDeSugerencias getInstancia() {
        if (instancia == null) {
            instancia = new AdministracionDeSugerencias();
        }
        return instancia;
    }

    /*
    public void verificarSiEstaCercaElMiembro(Miembro miembro) {
        List<Establecimiento> establecimientosCercanos = RepositorioEntidades.getInstancia().getEntidades().stream().
                flatMap(unaEntidad -> unaEntidad.getListaEstablecimientos().stream()).
                filter(unEstablecimiento->unEstablecimiento.estaCerca(miembro.getLocalidad()) && unEstablecimiento.esEstablecimientoConIncidentes()).toList();
        if (establecimientosCercanos.size() > 0) {
            this.enviarNotificacionSugerencia(miembro, establecimientosCercanos);
        }
    }
*/

/*
    public void recibirLocalizacionDeMiembro(Miembro miembro, Localidad localidad){
        List<Establecimiento> establecimientosCercanos = RepositorioEntidades.getInstancia().getEntidades().stream().
                flatMap(unaEntidad -> unaEntidad.getListaEstablecimientos().stream()).
                filter(unEstablecimiento->unEstablecimiento.estaCerca(localidad) && unEstablecimiento.esEstablecimientoConIncidentes()).toList();
        if (establecimientosCercanos.size() > 0) {
            this.enviarNotificacionSugerencia(miembro, establecimientosCercanos);
        }
    }
*/

    public void solicitarNotificacion(Miembro miembro, Establecimiento establecimiento){
        Notificacion notificacion = new Notificacion();
        notificacion.setAsunto("Revision manual de incidente");
        notificacion.setCuerpo("Estimado usuario, " +
                "¿podría acercarse a la siguiente ubicacion para cerrar el reporte de su comunidad"
                + establecimiento.getNombre());
        Notificador.getInstancia().enviarNotificacion(miembro, notificacion);
    }

    private void enviarNotificacionSugerencia(Miembro miembro, List<Establecimiento> establecimientosCerca) {
        establecimientosCerca.forEach(unEstablecimiento ->
                this.solicitarNotificacion(miembro, unEstablecimiento));
    }
}
