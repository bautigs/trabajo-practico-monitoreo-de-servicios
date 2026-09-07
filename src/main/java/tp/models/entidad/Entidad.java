package tp.models.entidad;

import lombok.Getter;
import lombok.Setter;
import tp.models.comunidad.Comunidad;
import tp.models.persistencia.Persistente;
import tp.models.servicios.Incidente;
import tp.models.servicios.Servicio;

import jakarta.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "entidad")
@Getter
@Setter
public class Entidad extends Persistente {

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "tipoEntidad_id", referencedColumnName = "id")
    private TipoEntidad tipoEntidad;

    @OneToMany(mappedBy = "entidadALaQuePertenece")
    private List<Establecimiento> listaEstablecimientos;

    // ubicación??

    public Entidad() {
        this.listaEstablecimientos = new ArrayList<>();
    }

    public void agregarEstablecimientos(Establecimiento ... establecimientos) {
        Collections.addAll(this.listaEstablecimientos, establecimientos);
    }

    public int getCantEstablecimientos() {
        return listaEstablecimientos.size();
    }

    public List<Incidente> obtenerIncidentesPorEntidad(){
        return this.getListaEstablecimientos()
            .stream()
            .map(Establecimiento::obtenerIncidentesDelEstablecimiento)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    public List<Servicio> obtenerServiciosPorEntidad(){
        return this.getListaEstablecimientos()
            .stream()
            .map(Establecimiento::getServicios)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    public int getCantIncidentes(){
        return this.obtenerIncidentesPorEntidad().size();
    }



    public int promedioDeCierreDeIncidentes(){
        return (int) this.obtenerIncidentesPorEntidad().
            stream().
            map(Incidente::getDuracion).
            collect(Collectors.toList()).
            stream().
            reduce(Duration.ZERO, Duration::plus).
            toSeconds() / (this.getCantIncidentes() == 0 ? 1 : this.getCantIncidentes());
    }


    public int getCantReportesParaRanking() {
       return this.obtenerServiciosPorEntidad()
            .stream()
            .mapToInt(Servicio::cantidadIncidentesParaRanking)
            .sum();
    }

    public int getCantAfectados() {
        int cantAfectados = 0;
       Set<Comunidad> comunidades = this.obtenerIncidentesPorEntidad()
            .stream().map(incidente -> incidente.getComunidad()).collect(Collectors.toSet());

            for(Comunidad comunidad : comunidades){
                cantAfectados += comunidad.getCantAfectados();
            }
            return cantAfectados;
    }
}
