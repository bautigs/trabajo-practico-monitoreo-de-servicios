package tp.models.builders;

import tp.models.comunidad.Persona;
import tp.models.entidad.Empresa;
import tp.models.notificador.NoExisteFormatoException;

public class EmpresBuilder {
    private Empresa empresa = new Empresa();

    public EmpresBuilder nombre(String nombre) throws NoExisteFormatoException {
        empresa.setNombre(nombre);
        return this;
    }

    public EmpresBuilder responsable(Persona persona){
        empresa.setResponsableDesignado(persona);
        return this;
    }


    public Empresa build(){
        return this.empresa;
    }
}
