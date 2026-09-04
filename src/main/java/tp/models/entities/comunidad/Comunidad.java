package tp.models.entities.comunidad;

import lombok.Getter;
import lombok.Setter;
import tp.models.entities.entidad.Establecimiento;
import tp.models.entities.persistencia.Persistente;
import tp.models.entities.servicios.Incidente;
import tp.models.entities.servicios.Servicio;
import tp.models.entities.servicios.TipoServicio;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// import transporte.Estacion;

@Entity
@Table(name = "comunidad")
@Getter
@Setter
public class Comunidad extends Persistente {

  @Column(name = "nombre")
  private String nombre;

  @OneToMany(mappedBy = "comunidad")
  private List<Miembro> miembros = new ArrayList<>();

  @ManyToMany
  private List<TipoServicio> serviciosDeInteres = new ArrayList<>();

  @OneToMany(mappedBy = "comunidad")
  private List<Incidente> listadoDeIncidentesReportados = new ArrayList<>();
  public Comunidad() {

  }

  public int getCantMiembros() {
    return this.miembros.size();
  }

  public void definirServicio(String unaDescripcion) {
    Servicio unServicio = new Servicio();
  }

  public void agregarReporteIncidente(Incidente reporteDeIncidente){
    this.listadoDeIncidentesReportados.add(reporteDeIncidente);
  }

  public void  agregarServiciosDeInteres(TipoServicio... unosServicios){
    Collections.addAll(this.serviciosDeInteres,unosServicios);
  }

  public void agregaMiembro(Miembro ... miembro) {
    Collections.addAll(this.miembros, miembro);
  }

  public void eliminarMiembro(Miembro miembro) {
    this.miembros.remove(miembro);
  }

  public int getCantAfectados() {
    return this.miembros.stream()
        .filter(m -> m.getCondicionDeMiembro() == CondicionDeMiembro.AFECTADO)
        .toList()
        .size();
  }

  public List<Establecimiento> establecimientosSegunComunidad(List<Establecimiento> establecimientos) {
    return establecimientos.stream()
            .filter(establecimiento ->
                    establecimiento.getServicios().stream()
                            .map(servicio -> servicio.getTipoServicio().getNombre())
                            .anyMatch(nombre -> serviciosDeInteres.stream()
                                    .anyMatch(tipoServicio -> tipoServicio.getNombre().equals(nombre))))
            .toList();
  }


}