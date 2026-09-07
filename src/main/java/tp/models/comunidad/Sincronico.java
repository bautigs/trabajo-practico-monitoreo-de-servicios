package tp.models.comunidad;

import tp.models.notificador.Notificacion;

public class Sincronico implements ConfiguracionRecepcion {
    public void gestionarNotificacion(Notificacion unaNotificacion){
        unaNotificacion.getEstrategiaDeNotificacion().notificar(unaNotificacion);
    }
}
