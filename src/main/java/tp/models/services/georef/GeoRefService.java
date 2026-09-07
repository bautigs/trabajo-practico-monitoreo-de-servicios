package tp.models.services.georef;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoRefService {

    @GET("provincias")
    Call<ListaProvincias> provincias();

    @GET("municipios")
    Call<ListaMunicipios> municipios();

    @GET("municipios")
    Call<ListaMunicipios> municipios(@Query("max") Integer max);

    @GET("localidades")
    Call<ListaLocalidades> localidades(@Query("max") Integer max);

    @GET("municipios") Call<ListaMunicipios> municipiosPorNombre(@Query("nombre") String nombre,@Query("max") Integer cantidad);

    @GET("provincias") Call<ListaProvincias> provinciasPorNombre(@Query("nombre") String nombre,@Query("max") Integer cantidad);

    @GET("provincias") Call<ListaProvincias> provinciasPorID(@Query("id") Integer id,@Query("max") Integer cantidad);

    @GET("municipios") Call<ListaMunicipios> municipiosPorProvincia(@Query("provincia") Integer id,@Query("max") Integer cantidad);

    @GET("localidades") Call<ListaLocalidades> localidadesPorNombre(@Query("nombre") String nombre,@Query("max") Integer cantidad);

    @GET("localidades") Call<ListaLocalidades> localidadesPorMunicipio(@Query("municipio") Integer id,@Query("max") Integer cantidad);

    @GET("localidades") Call<ListaLocalidades> localidadesGenerico();

}
