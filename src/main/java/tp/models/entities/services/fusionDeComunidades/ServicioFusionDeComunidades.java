package tp.models.entities.services.fusionDeComunidades;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;

public class ServicioFusionDeComunidades {
    private static ServicioFusionDeComunidades instancia = null;

    private static final String urlAPI = "http://localhost:8080/";

    private Retrofit retrofit;

    private ServicioFusionDeComunidades(){
        this.retrofit = new Retrofit.Builder().baseUrl(urlAPI).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static ServicioFusionDeComunidades getInstancia(){
        if(instancia == null){
            instancia = new ServicioFusionDeComunidades();
        }
        return instancia;
    }

    public ArrayList<PropuestaDeFusionModel> sugerirFusiones(PeticionModel peticionModel) throws IOException{
        FusionDeComunidadesService fusionDeComunidadesService = this.retrofit.create(FusionDeComunidadesService.class);
        Call<ArrayList<PropuestaDeFusionModel>> propuestasCall = fusionDeComunidadesService.sugerirFusiones(peticionModel);
        Response<ArrayList<PropuestaDeFusionModel>> propuestasResponse = propuestasCall.execute();
        return propuestasResponse.body();
    }

    public ComunidadModel fusionarComunidades(PropuestaDeFusionModel requestFusionarComunidades) throws IOException{
        FusionDeComunidadesService fusionDeComunidadesService = this.retrofit.create(FusionDeComunidadesService.class);
        Call<ComunidadModel> fusionCall = fusionDeComunidadesService.fusionarComunidades(requestFusionarComunidades);
        Response<ComunidadModel> fusionResponse = fusionCall.execute();
        return fusionResponse.body();
    }


}
