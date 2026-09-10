package tp.services.georef;

import java.util.List;

public class ListaMunicipios {

    public int cantidad;
    public int inicio;
    public Parametro parametros;
    public int total;

    public List<Municipio> municipios;

    private class Parametro {

        public String nombre;
        public List<String> provincia;
        public List<String> campos;
        public int max;
    }
}
