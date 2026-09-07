package tp.models.services.georef;

import java.util.List;

public class ListaProvincias {

    public int cantidad;
    public int inicio;
    public int total;
    public Parametro parametros;

    public List<Provincia> provincias;

    private class Parametro {
        public List<String> campos;

        public String nombre;
    }
}
