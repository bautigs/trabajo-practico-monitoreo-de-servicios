package tp.models.entities.notificador;

import tp.models.entities.comunidad.Comunidad;
import tp.models.entities.comunidad.Miembro;
import tp.models.entities.comunidad.Persona;
import tp.models.entities.servicios.Incidente;
import tp.models.entities.servicios.Servicio;

import java.time.LocalDateTime;

public class Notificador {

    private static Notificador instancia = null;

    public static Notificador getInstancia(){
        if(instancia == null){
            instancia = new Notificador();
        }
        return instancia;
    }

    public void avisarMiembros(Comunidad comunidad, Notificacion unaNotificacion){

        comunidad.getMiembros().forEach(miembro -> {
            if (miembro.getEstrategia() == null || miembro.getConfiguracion() == null) {
                    this.enviarNotificacion(miembro.getPersona(), unaNotificacion);
            }else {
                this.enviarNotificacion(miembro, unaNotificacion);
            }
        });
    }

    public void avisarInteresadosInit(Servicio servicio, String descripcion, LocalDateTime unaFechaApertura){

        Incidente incidenteParticular = new Incidente();
        incidenteParticular.aperturaDeIncidente(descripcion , servicio.descripcionDelLugar(),unaFechaApertura);
        servicio.getSuscriptores().forEach(suscriptor -> this.enviarNotificacion(suscriptor, incidenteParticular.getNotificacion()));
    }

    public void avisarInteresadosCierre(Servicio servicio, LocalDateTime unaFechaCierre){

        Incidente incidenteParticular = new Incidente();
        incidenteParticular.cierreDeIncidente(unaFechaCierre, null);
        servicio.getSuscriptores().forEach(suscriptor -> this.enviarNotificacion(suscriptor, incidenteParticular.getNotificacion()));
    }


    public void enviarNotificacion(Miembro miembro, Notificacion unaNotificacion){

        unaNotificacion.setMailDeContacto(miembro.getPersona().getMailDeContacto());
        unaNotificacion.setNumeroDeContacto(miembro.getPersona().getNumeroDeContacto());
        unaNotificacion.setEstrategiaDeNotificacion(miembro.getEstrategia());
        miembro.getConfiguracion().gestionarNotificacion(unaNotificacion);
    }

    public void enviarNotificacion(Persona persona, Notificacion unaNotificacion){

        unaNotificacion.setMailDeContacto(persona.getMailDeContacto());
        unaNotificacion.setNumeroDeContacto(persona.getNumeroDeContacto());
        unaNotificacion.setEstrategiaDeNotificacion(persona.getEstrategia());
        persona.getConfiguracion().gestionarNotificacion(unaNotificacion);
    }
}

