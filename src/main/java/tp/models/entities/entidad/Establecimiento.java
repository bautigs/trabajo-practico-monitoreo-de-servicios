package tp.models.entities.entidad;

import lombok.Getter;
import lombok.Setter;
import tp.models.entities.persistencia.Persistente;
import tp.models.entities.services.georef.Localidad;
import tp.models.entities.services.georef.Municipio;
import tp.models.entities.services.georef.Provincia;
import tp.models.entities.servicios.Incidente;
import tp.models.entities.servicios.Servicio;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "establecimiento")
@Getter
@Setter
public class Establecimiento extends Persistente {

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "localidad_id", referencedColumnName = "id")
    private Localidad localidad;

    @ManyToOne
    @JoinColumn(name = "provincia_id", referencedColumnName = "id")
    private Provincia provincia;

    @ManyToOne
    @JoinColumn(name = "municipio_id", referencedColumnName = "id")
    private Municipio municipio;

    @OneToMany
    @JoinColumn(name = "establecimiento_id", referencedColumnName = "id")
    private List<Servicio> servicios;

    @ManyToOne
    @JoinColumn(name = "entidad_id", referencedColumnName = "id")
    private Entidad entidadALaQuePertenece;

    public Establecimiento()  {
        this.servicios = new ArrayList<>();
    }
    public void agregarServicios(Servicio ... servicios) {
        Collections.addAll(this.servicios, servicios);
    }

    public int getCantServicios() {
        return servicios.size();
    }

    public void eliminarServicioSegunDescripcion(String descripcionServicio) {
        this.servicios.removeIf(s -> s.getTipoServicio().getNombre().equals(descripcionServicio));
    }

    public Servicio servicioSegunDescripcion(String descripcionServicio) {
        return this.servicios.stream().filter(s -> descripcionServicio.equals(s.getTipoServicio().getNombre())).findFirst().orElse(null);
    }

    public boolean esEstablecimientoConIncidentes(){
        return !this.obtenerIncidentesDelEstablecimiento().isEmpty();
    }

    public List<Incidente> obtenerIncidentesDelEstablecimiento(){
        List<Incidente> incidentes = new ArrayList<>();

        return this.getServicios()
            .stream()
            .flatMap(unServicio->unServicio.getIncidentesAbiertos().stream())
            .collect(Collectors.toList());
    }

    public List<Servicio> obtenerServiciosConIncidente() {
        return this.servicios.stream().filter(Servicio::tieneIncidentes).toList();
    }

    public boolean estaEnMunicipio(Municipio municipio){
        return this.localidad.municipio == municipio;
    }

    public boolean estaEnLocalidad(Localidad localidad){
        return this.localidad == localidad;
    }

    public boolean estaEnProvincia(Provincia provincia) {
        return this.localidad.getMunicipio().getProvincia().equals(provincia);
    }
}
