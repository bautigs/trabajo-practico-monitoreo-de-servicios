package tp.models.builders;

import tp.models.entidad.Entidad;
import tp.models.entidad.Establecimiento;
import tp.models.servicios.Servicio;
import tp.services.georef.Localidad;
import tp.services.georef.Municipio;
import tp.services.georef.Provincia;

import java.util.List;

public class EstablecimientoBuilder {

    private Establecimiento establecimiento;

    public EstablecimientoBuilder() {
        this.establecimiento = new Establecimiento();
    }

    public EstablecimientoBuilder nombre(String nombre) {
        this.establecimiento.setNombre(nombre);
        return this;
    }

    public EstablecimientoBuilder provincia(Provincia provincia) {
        this.establecimiento.setProvincia(provincia);
        return this;
    }

    public EstablecimientoBuilder localidad(Localidad localidad) {
        this.establecimiento.setLocalidad(localidad);
        return this;
    }

    public EstablecimientoBuilder municipio(Municipio municipio) {
        this.establecimiento.setMunicipio(municipio);
        return this;
    }

    public EstablecimientoBuilder servicios(List<Servicio> servicios) {
        this.establecimiento.setServicios(servicios);
        return this;
    }

    public EstablecimientoBuilder entidad(Entidad entidad) {
        this.establecimiento.setEntidadALaQuePertenece(entidad);
        return this;
    }

    public Establecimiento build() {
        return this.establecimiento;
    }
}
