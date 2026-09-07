package tp.models.comunidad;

import lombok.Getter;
import lombok.Setter;
import tp.models.converters.ConfiguracionRecepcionConverter;
import tp.models.converters.EstrategiaDeNotificacionConverter;
import tp.models.notificador.EstrategiaDeNotificacion;
import tp.models.notificador.Factory;
import tp.models.notificador.NoExisteFormatoException;
import tp.models.notificador.Notificador;
import tp.models.persistencia.Persistente;
import tp.models.servicios.Estado;
import tp.models.servicios.Incidente;
import tp.models.servicios.IncidenteBuilder;
import tp.models.servicios.Servicio;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "miembro")
@Getter
@Setter
public class Miembro extends Persistente {

  @Convert(converter = EstrategiaDeNotificacionConverter.class)
  @Column(name = "estrategiaNotificacion")
  private EstrategiaDeNotificacion estrategia;

  @Convert(converter = ConfiguracionRecepcionConverter.class)
  @Column(name = "configuracionRecepcion")
  private ConfiguracionRecepcion configuracion;


  @ManyToOne
  @JoinColumn(name = "comunidad_id", referencedColumnName = "id")
  private Comunidad comunidad;

  @Enumerated(EnumType.STRING)
  private Rol rol;

  @Enumerated(EnumType.STRING)
  private CondicionDeMiembro condicionDeMiembro;

  @ManyToOne
  @JoinColumn(name = "persona_id", referencedColumnName = "id")
  private Persona persona;

  public void setEstrategia(String estrategia) throws NoExisteFormatoException {
    this.estrategia = Factory.crear(estrategia);
  }

  public void setearConfiguracionSincronica() {
    this.configuracion = new Sincronico();
  }

  public void setearConfiguracionAsincronica(List<Integer> horarios) {
    this.configuracion = new Asincronico();
  }

  public Incidente abrirIncidente(Servicio servicio, String descripcion) {
    Notificador unNotificador = Notificador.getInstancia();

    LocalDateTime fechaApertura = LocalDateTime.now();

    unNotificador.avisarInteresadosInit(servicio, descripcion, fechaApertura);

    Incidente incidente = new IncidenteBuilder().servicio(servicio).comunidad(this.getComunidad()).estado(Estado.ABIERTO).miembroDeApertura(this).fechaCierre(null).build();
    incidente.aperturaDeIncidente(descripcion, servicio.descripcionDelLugar(), fechaApertura);
    comunidad.agregarReporteIncidente(incidente);
    unNotificador.avisarMiembros(comunidad, incidente.getNotificacion());
    servicio.agregarIncidente(incidente);

    return incidente;
  }

  public void cierreDeIncidente(Incidente incidente) {
    Notificador unNotificador = Notificador.getInstancia();

    LocalDateTime fechaCierre = LocalDateTime.now();

    incidente.cierreDeIncidente(fechaCierre, this);
    unNotificador.avisarMiembros(incidente.getComunidad(), incidente.getNotificacion());

    unNotificador.avisarInteresadosCierre(incidente.getServicio(), fechaCierre);

  }

  private List<Comunidad> comunidadesInteresadas(Servicio servicio){
    List<Comunidad> comunidadesInteresadas = new ArrayList<>();
    comunidadesInteresadas = this.getPersona().getComunidades().stream()
            .filter(comunidad ->
                    comunidad
                            .getServiciosDeInteres().contains(servicio.getTipoServicio())
                            && !servicio.existeEnComunidad(comunidad))
            .toList();

    return comunidadesInteresadas;
  }
}
