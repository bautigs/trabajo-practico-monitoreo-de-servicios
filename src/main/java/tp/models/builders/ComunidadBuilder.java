package tp.models.builders;

import tp.models.comunidad.Comunidad;
import tp.models.comunidad.Miembro;
import tp.models.servicios.TipoServicio;

import java.util.List;

public class ComunidadBuilder {

    private Comunidad comunidad;

    public ComunidadBuilder() {
        this.comunidad = new Comunidad();
    }

    public ComunidadBuilder nombre(String nombre) {
        this.comunidad.setNombre(nombre);
        return this;
    }

    public ComunidadBuilder miembros(List<Miembro> miembros) {
        this.comunidad.setMiembros(miembros);
        return this;
    }

    public ComunidadBuilder tipoServicios(List<TipoServicio> tipoServicios){
        this.comunidad.setServiciosDeInteres(tipoServicios);
        return this;
    }

    public Comunidad build() {
        return this.comunidad;
    }


}
