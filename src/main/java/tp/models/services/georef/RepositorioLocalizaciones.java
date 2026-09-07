package tp.models.services.georef;

import lombok.Getter;

import java.util.List;

public class RepositorioLocalizaciones {

    private static RepositorioLocalizaciones instancia = null;

    @Getter
    private static List<Provincia> listaLocalizaciones;

    private static List<Integer> listaIDsProvincias;

/*
    public static RepositorioLocalizaciones getInstancia() throws IOException {
        if(instancia == null) {
            instancia = new RepositorioLocalizaciones();
            listaIDsProvincias = new ArrayList<>();
            Collections.addAll(listaIDsProvincias,54,38*/
/*,74,70,30,78,62,26,14,50,46,10,42,86,18,82,90,58,66,22,34,38,2,6,94*//*
);
            RepositorioLocalizaciones.cargarLista();
        }

        return instancia;
    }
*/

    public List<Provincia> getListaLoc(){
        return listaLocalizaciones;
    }

    public Provincia obtenerProvincia(String nombreProvincia){
        return getListaLoc().stream().filter(provincia1 -> provincia1.nombre.equals(nombreProvincia)).toList().get(0);
    }

/*
    private static void cargarLista() throws IOException {

        listaLocalizaciones = new ArrayList<>();

        ServicioGeoRef sgr = ServicioGeoRef.getInstancia();
        Provincia prov;

        for(Integer i : RepositorioLocalizaciones.listaIDsProvincias){
                prov = sgr.provinciasPorID(i,1).provincias.get(0);
                ListaMunicipios listaMunicipios = sgr.municipiosPorProvincia(i);

                for(Municipio municipio : listaMunicipios.municipios) {
                    ListaLocalidades listaLocalidades = sgr.localidadesPorMunicipio(municipio.id);
                    municipio.setListaLocalidades(listaLocalidades.localidades);
                }
                prov.setListaMunicipios(listaMunicipios.municipios);
                listaLocalizaciones.add(prov);
        }
    }
*/
}
