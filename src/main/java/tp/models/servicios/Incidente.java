package tp.models.servicios;

import lombok.Getter;
import lombok.Setter;
import tp.models.comunidad.Comunidad;
import tp.models.comunidad.Miembro;
import tp.models.notificador.Notificacion;
import tp.models.persistencia.Persistente;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "incidente")
@Getter
@Setter
public class Incidente extends Persistente {

    @ManyToOne
    @JoinColumn(name = "comunidad_id", referencedColumnName = "id")
    private Comunidad comunidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    @Column(name = "fechaApertura", columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaApertura;

    @Column(name = "fechaCierre", columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaCierre;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "miembroDeApertura_id", referencedColumnName = "id")
    private Miembro miembroDeApertura;

    @ManyToOne
    @JoinColumn(name = "miembroDeCierre_id", referencedColumnName = "id")
    private Miembro miembroDeCierre;

    @Transient
    private Notificacion notificacion = new Notificacion();

    @Column(name = "descripcionLugar")
    private String descripcionLugar;

    @ManyToOne
    @JoinColumn(name = "servicio_id", referencedColumnName = "id")
    private Servicio servicio;

    public void aperturaDeIncidente(String descripcionDelIncidente, String lugar, LocalDateTime unaFechaApertura){
        String asunto = "Apertura de incidente" + " en " + lugar;
        setDescripcionLugar(lugar);
        setDescripcion(descripcionDelIncidente);
        setFechaApertura(LocalDateTime.now());
        notificacion.setAsunto(asunto);
        notificacion.setCuerpo(descripcionDelIncidente);
    }

    public void cierreDeIncidente(LocalDateTime unaFechaCierre, Miembro miembro){
        String asunto = "Cierre de incidente" + " en " + descripcionLugar;
        setFechaCierre(unaFechaCierre);
        notificacion.setAsunto(asunto);
        notificacion.setCuerpo(descripcion);
        this.setEstado(Estado.CERRADO);
        this.setMiembroDeCierre(miembro);
    }

    public Duration getDuracion() {
        if (this.getFechaCierre() != null) {
            return Duration.between(this.getFechaApertura(), this.getFechaCierre());
        }
        return Duration.ZERO;
    }

    public Boolean isAbierto(){
        return this.getEstado().toString().equals("ABIERTO");
    }

    public String formatDate(){
        return this.fechaApertura.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

}

