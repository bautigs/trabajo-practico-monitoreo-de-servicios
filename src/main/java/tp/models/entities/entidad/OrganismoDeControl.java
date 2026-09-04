package tp.models.entities.entidad;

import lombok.Getter;
import lombok.Setter;
import tp.models.entities.comunidad.Persona;
import tp.models.entities.persistencia.Persistente;
import tp.models.entities.ranking.exportador.Exportador;
import tp.models.entities.servicios.TipoServicio;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organismoControl")
@Getter
@Setter
public class OrganismoDeControl extends Persistente {

    @Column(name = "nombre")
    private String nombre;

    @Transient
    private Persona responsableDesignado;

    @ManyToOne
    @JoinColumn(name = "tipoServicio_id", referencedColumnName = "id")
    private TipoServicio servicioQueRegula;


    @Transient
    private List<Empresa> listaEmpresasQueRegula;

    public OrganismoDeControl(){
        this.listaEmpresasQueRegula = new ArrayList<>();
    }

    public void agregarEmpresaAControlar(Empresa empresa){
        listaEmpresasQueRegula.add(empresa);
    }



    public List<Entidad> obtenerEntidadesCorrespondientes(List<Entidad> listaEntidades){
        return this.listaEmpresasQueRegula.stream().flatMap(unaEmpresa -> unaEmpresa.obtenerEntidadesPropias(listaEntidades).stream()).toList();
    }


    public void solicitarInformeSemanal(String formato){
        Exportador exportador = Exportador.getInstancia();
        exportador.generarInformeSemanal(this,formato);
    }
}
