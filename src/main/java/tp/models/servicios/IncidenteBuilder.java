package tp.models.servicios;

import tp.models.comunidad.Comunidad;
import tp.models.comunidad.Miembro;
import java.time.LocalDateTime;

public class IncidenteBuilder {

  private Incidente incidente;
/*
  // .comunidad()
        .estado(Estado.ABIERTO)
        .descripcion(context.formParam("descripcion"))
      .descripcionLugar(context.formParam("descripcionLugar"))
      .fechaApertura(LocalDateTime.now())
  // .miembroDeApertura()
*/

  public IncidenteBuilder() {
    this.incidente = new Incidente();
  }

  public IncidenteBuilder comunidad(Comunidad comunidad) {
    this.incidente.setComunidad(comunidad);
    return this;
  }

  public IncidenteBuilder estado(Estado estado) {
    this.incidente.setEstado(estado);
    return this;
  }

  public IncidenteBuilder descripcion(String descripcion) {
    this.incidente.setDescripcion(descripcion);
    return this;
  }

  public IncidenteBuilder descripcionLugar(String descripcionLugar) {
    this.incidente.setDescripcionLugar(descripcionLugar);
    return this;
  }

  public IncidenteBuilder fechaApertura(LocalDateTime fechaApertura) {
    this.incidente.setFechaApertura(fechaApertura);
    return this;
  }

  public IncidenteBuilder fechaCierre(LocalDateTime fechaCierre) {
    this.incidente.setFechaCierre(fechaCierre);
    return this;
  }

  public IncidenteBuilder miembroDeApertura(Miembro miembroDeApertura) {
    this.incidente.setMiembroDeApertura(miembroDeApertura);
    return this;
  }

  public IncidenteBuilder servicio(Servicio servicio) {
   this.incidente.setServicio(servicio);
   return this;
  }

  public Incidente build() {
    return this.incidente;
  }
}
