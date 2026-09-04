package tp.models.entities.comunidad;

import lombok.Getter;
import lombok.Setter;
import tp.models.entities.converters.ConfiguracionRecepcionConverter;
import tp.models.entities.converters.EstrategiaDeNotificacionConverter;
import tp.models.entities.entidad.Entidad;
import tp.models.entities.entidad.Establecimiento;
import tp.models.entities.notificador.EstrategiaDeNotificacion;
import tp.models.entities.notificador.Factory;
import tp.models.entities.notificador.NoExisteFormatoException;
import tp.models.entities.persistencia.Persistente;
import tp.models.entities.services.georef.Localidad;
import tp.models.entities.services.georef.Municipio;
import tp.models.entities.services.georef.Provincia;
import tp.models.entities.servicios.Servicio;
import tp.models.entities.servicios.TipoServicio;
import tp.models.repositories.RepositorioEstablecimientos;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@Table(name = "persona")
public class Persona extends Persistente {

  @OneToMany(mappedBy = "persona")
  private Set<Miembro> membresias = new HashSet<>();

  @OneToOne
  @JoinColumn(name = "usuario_id", referencedColumnName = "id")
  private Usuario usuario;

  @Column(name = "nombreApellido")
  private String nombreApellido;

  @Column(name = "mailDeContacto")
  private String mailDeContacto;

  @Column(name = "numeroDeContacto")
  private String numeroDeContacto;

  @Convert(converter = EstrategiaDeNotificacionConverter.class)
  @Column(name = "estrategiaNotificacion")
  private EstrategiaDeNotificacion estrategia;

  @Convert(converter = ConfiguracionRecepcionConverter.class)
  @Column(name = "configuracionRecepcion")
  private ConfiguracionRecepcion configuracion;

  @Setter
  @ManyToMany
  private List<TipoServicio> tiposDeServiciosDeInteres;

  @ManyToMany
  private List<Entidad> entidadesDeInteres;

  @ManyToOne
  @JoinColumn(name = "provincia_id", referencedColumnName = "id")
  private Provincia provincia;

  @ManyToOne
  @JoinColumn(name = "municipio_id", referencedColumnName = "id")
  private Municipio municipio;

  @ManyToOne
  @JoinColumn(name = "localidad_id", referencedColumnName = "id")
  private Localidad localidad;

  @Transient
  private String estrategiaString;

  @Enumerated(EnumType.STRING)
  private RolPersona rolPersona;



  public void setEstrategia(String estrategia) throws NoExisteFormatoException {
    this.estrategia = Factory.crear(estrategia);
  }

  public void setearConfiguracionSincronica() {
    this.configuracion = new Sincronico();
  }

  public void setearConfiguracionAsincronica(List<Integer> horarios) {
    this.configuracion = new Asincronico();
  }


  private List<Establecimiento> establecimientosDeInteres() {
    List<Establecimiento> establecimientosInteres = new ArrayList<>();
    entidadesDeInteres.forEach(entidad -> establecimientosInteres.
            addAll(entidad.getListaEstablecimientos()));

    return establecimientosInteres;
  }

  public List<Servicio> servicioDeInteres(){
    List<Servicio> serviciosDeEntidadesDeInteres;
    serviciosDeEntidadesDeInteres = new ArrayList<>();

    this.establecimientosDeInteres().forEach(establecimiento -> establecimiento.getServicios().stream()
            .filter(servicio -> this.tiposDeServiciosDeInteres.contains(servicio.getTipoServicio()))
            .forEach(serviciosDeEntidadesDeInteres::add));

    return serviciosDeEntidadesDeInteres;
  }

  public void setMunicipio(Municipio unMunicipio){
    this.municipio = unMunicipio;
  }

  public void setLocalidad(Localidad unaLocalidad){
    this.localidad = unaLocalidad;
  }

  public void setProvinicia(Provincia unaProvincia){
    this.provincia = unaProvincia;
  }

  public List<Servicio> serviciosEnMunicipio(){
    List<Establecimiento> establecimientos = RepositorioEstablecimientos.getInstancia().all();

    Stream<Servicio> ret;
    ret = establecimientos.stream().filter(e -> e.estaEnMunicipio(this.municipio)).flatMap(e -> e.getServicios().stream().filter(servicio -> this.tiposDeServiciosDeInteres.contains(servicio.getTipoServicio())));

    return ret.toList();
  }

  public List<Servicio> serviciosEnLocalidad(){
    List<Establecimiento> establecimientos = RepositorioEstablecimientos.getInstancia().all();

    Stream<Servicio> ret;
    ret = establecimientos.stream().filter(e -> e.estaEnLocalidad(this.localidad)).flatMap(e -> e.getServicios().stream().filter(servicio -> this.tiposDeServiciosDeInteres.contains(servicio.getTipoServicio())));

    return ret.toList();
  }

  public List<Servicio> serviciosEnProvincia(){
    List<Establecimiento> establecimientos = RepositorioEstablecimientos.getInstancia().all();

    Stream<Servicio> ret;
    ret = establecimientos.stream().filter(e -> e.estaEnProvincia(this.provincia)).flatMap(e -> e.getServicios().stream().filter(servicio -> this.tiposDeServiciosDeInteres.contains(servicio.getTipoServicio())));

    return ret.toList();
  }

  public void suscribirse() {
    this.servicioDeInteres().
            forEach(servicio -> servicio.
                    getSuscriptores().add(this));
  }

  public Boolean tieneComunidades(){
    return !membresias.isEmpty();
  }
  public List<Comunidad> getComunidades(){
    return membresias.stream().map(Miembro::getComunidad).toList();
  }
  public boolean isAdmin(){
    return getRolPersona().equals(RolPersona.ADMIN);
  }

  public void agregarMembresia(Miembro miembro){
    this.membresias.add(miembro);
  }

  public void eliminarMembresia(Miembro miembro) {
    this.membresias.remove(miembro);
  }
}
