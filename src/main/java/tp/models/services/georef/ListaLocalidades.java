package tp.models.services.georef;

import java.util.List;

public class ListaLocalidades {
    public int cantidad;
    public int inicio;
    public List<Localidad> localidades;
    public Parametro parametros;
    public Integer total;

    public class Parametro{

        public String nombre;
        public List<String> municipio;
        public List<String> campos;
        public Integer max;
    }
}
