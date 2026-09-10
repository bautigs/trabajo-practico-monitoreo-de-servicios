package tp.services.fusionDeComunidades;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.ArrayList;

public interface FusionDeComunidadesService {

    @POST("comunidad/sugerirFusiones")
    Call<ArrayList<PropuestaDeFusionModel>> sugerirFusiones(@Body PeticionModel request);

    @POST("comunidad/fusionarComunidades")
    Call<ComunidadModel> fusionarComunidades(@Body PropuestaDeFusionModel request);

}
