package tp.models.services.georef;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;

public class ServicioGeoRef{

    private static ServicioGeoRef instancia = null;

    private static final String urlAPI = "https://apis.datos.gob.ar/georef/api/";

    private Retrofit retrofit;

    private ServicioGeoRef(){
        this.retrofit = new Retrofit.Builder().baseUrl(urlAPI).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static ServicioGeoRef getInstancia(){
        if(instancia == null){
            instancia = new ServicioGeoRef();
        }
        return instancia;
    }


    public ListaMunicipios municipiosPorNombre(String nombreProvincia,Integer max) throws IOException{
        GeoRefService geoRefService = this.retrofit.create(GeoRefService.class);
        Call<ListaMunicipios> requestMunicipiosPorNombre = geoRefService.municipiosPorNombre(nombreProvincia,max);
        Response<ListaMunicipios> responseMunicipiosPorNombre = requestMunicipiosPorNombre.execute();
        return responseMunicipiosPorNombre.body();
    }

    public ListaMunicipios municipiosPorProvincia(Integer id) throws IOException{
        GeoRefService geoRefService = this.retrofit.create(GeoRefService.class);
        Call<ListaMunicipios> requestmunicipiosPorProvincia = geoRefService.municipiosPorProvincia(id,1000);
        Response<ListaMunicipios> responsemunicipiosPorProvincia = requestmunicipiosPorProvincia.execute();
        return responsemunicipiosPorProvincia.body();
    }

    public ListaLocalidades localidadesPorNombre(String nombreLocalidad,Integer max) throws IOException{
        GeoRefService geoRefService = this.retrofit.create(GeoRefService.class);
        Call<ListaLocalidades> requestLocalidadesPorNombre = geoRefService.localidadesPorNombre(nombreLocalidad,max);
        Response<ListaLocalidades> responseLocalidadesPorNombre = requestLocalidadesPorNombre.execute();
        return responseLocalidadesPorNombre.body();
    }

    public ListaProvincias provinciasPorNombre(String nombreLocalidad,Integer max) throws IOException{
        GeoRefService geoRefService = this.retrofit.create(GeoRefService.class);
        Call<ListaProvincias> requestProvinciasPorNombre = geoRefService.provinciasPorNombre(nombreLocalidad,max);
        Response<ListaProvincias> responseProvinciasPorNombre= requestProvinciasPorNombre.execute();
        return responseProvinciasPorNombre.body();
    }

    public ListaProvincias provinciasPorID(Integer id,Integer max) throws IOException{
        GeoRefService geoRefService = this.retrofit.create(GeoRefService.class);
        Call<ListaProvincias> requestProvinciasPorID = geoRefService.provinciasPorID(id,max);
        Response<ListaProvincias> responseProvinciasPorID= requestProvinciasPorID.execute();
        return responseProvinciasPorID.body();
    }

    public ListaLocalidades localidadesGenerico() throws IOException{
        GeoRefService geoRefService = this.retrofit.create(GeoRefService.class);
        Call<ListaLocalidades> requestLocalidadesPorNombre = geoRefService.localidadesGenerico();
        Response<ListaLocalidades> responseLocalidadesPorNombre = requestLocalidadesPorNombre.execute();
        return responseLocalidadesPorNombre.body();
    }

    public ListaLocalidades localidadesPorMunicipio(Integer id) throws IOException{
        GeoRefService geoRefService = this.retrofit.create(GeoRefService.class);
        Call<ListaLocalidades> requestLocalidadesPorMunicipio = geoRefService.localidadesPorMunicipio(id,1000);
        Response<ListaLocalidades> responseLocalidadesPorMunicipio = requestLocalidadesPorMunicipio.execute();
        return responseLocalidadesPorMunicipio.body();
    }

    public ListaProvincias listaProvincias() throws IOException {
        GeoRefService georefService = this.retrofit.create(GeoRefService.class);
        Call<ListaProvincias> requestProvincias = georefService.provincias();
        Response<ListaProvincias> responseProvincias = requestProvincias.execute();
        return responseProvincias.body();
    }

    public ListaMunicipios municipios() throws IOException {
        GeoRefService geoRefService = this.retrofit.create(GeoRefService.class);
        Call<ListaMunicipios> municipiosCall = geoRefService.municipios(1900);
        Response<ListaMunicipios> municipiosResponse = municipiosCall.execute();
        return municipiosResponse.body();
    }

    public ListaLocalidades localidades() throws IOException {
        GeoRefService geoRefService = this.retrofit.create(GeoRefService.class);
        Call<ListaLocalidades> localidadesCall = geoRefService.localidades(4200);
        Response<ListaLocalidades> localidadesResponse = localidadesCall.execute();
        return localidadesResponse.body();
    }
}
