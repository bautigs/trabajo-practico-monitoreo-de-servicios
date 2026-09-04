package tp.models.entities.servicios;

import lombok.Getter;
import lombok.Setter;
import tp.models.entities.comunidad.Comunidad;
import tp.models.entities.comunidad.Persona;
import tp.models.entities.persistencia.Persistente;

import jakarta.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "servicio")
@Getter
@Setter
public class Servicio extends Persistente {

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "tipoServicio_id", referencedColumnName = "id")
    private TipoServicio tipoServicio;

    @Column(name = "estaActivo")
    private Boolean estaActivo;

    @Transient
    private List<Persona> suscriptores;

    @OneToMany
    @JoinColumn(name = "servicio_id", referencedColumnName = "id")
    private List<Incidente> incidentes;

    @Getter
    @Transient
    private List<Incidente> incidentesParaRanking;

    @Transient
    private Incidente ultimoIncidente;

    public Servicio() {
        this.estaActivo = true;
        this.suscriptores = new ArrayList<>();
        this.incidentes = new ArrayList<>();
        this.incidentesParaRanking = new ArrayList<>();
    }

    public List<Comunidad> comunidadesSuscriptas() {
        return suscriptores.stream()
                .flatMap(unSuscriptor -> unSuscriptor.getComunidades().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public String descripcionDelLugar() {
        return this.getTipoServicio().getNombre();
    }

    public boolean existeEnComunidad(Comunidad comunidad) {
        return incidentes.stream().anyMatch(incidente -> incidente.getComunidad().equals(comunidad));
    }

    public void agregarIncidente(Incidente incidente) {
        this.incidentes.add(incidente);
        this.analizarIncidente(incidente);
    }

    public void analizarIncidente(Incidente incidente) {
        if (this.incidentesParaRanking.isEmpty()) {
            this.ultimoIncidente = incidente;
            this.incidentesParaRanking.add(incidente);
        } else if (Duration.between(this.ultimoIncidente.getFechaApertura(), incidente.getFechaApertura()).toHours() > 24) {
            this.ultimoIncidente = incidente;
            this.incidentesParaRanking.add(incidente);
        }
    }

    public List<Incidente> getIncidentesAbiertos() {
        return this.incidentes
                .stream()
                .filter(i -> i.getEstado() == Estado.ABIERTO)
                .collect(Collectors.toList());
    }

    public boolean tieneIncidentes() {
        return !this.incidentes.isEmpty();
    }

    public Integer cantidadIncidentesParaRanking() {
        return this.incidentesParaRanking.size();
    }

    public List<Incidente> incidentesDeComunidad(Long idComunidad) {
        return this.getIncidentes().stream()
                .filter(incidente ->
                        incidente.getComunidad().getId().equals(idComunidad)).toList();

    }
}
