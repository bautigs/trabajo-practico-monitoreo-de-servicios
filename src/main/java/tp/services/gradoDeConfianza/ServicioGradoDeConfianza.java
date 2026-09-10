package tp.services.gradoDeConfianza;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class ServicioGradoDeConfianza {
    private static ServicioGradoDeConfianza instancia = null;

    private static final String urlAPI = "http://localhost:8081/";

    private Retrofit retrofit;

    private ServicioGradoDeConfianza() {
        this.retrofit = new Retrofit.Builder().baseUrl(urlAPI).addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ServicioGradoDeConfianza getInstancia() {
        if (instancia == null) {
            instancia = new ServicioGradoDeConfianza();
        }
        return instancia;
    }

    public List<ComunidadModelConfianza> comunidad(RequestComunidad requestComunidad) throws IOException {
        GradoDeConfianzaService gradoDeConfianzaService = this.retrofit.create(GradoDeConfianzaService.class);
        Call<List<ComunidadModelConfianza>> comunidadesCall = gradoDeConfianzaService.comunidad(requestComunidad);
        Response<List<ComunidadModelConfianza>> response = comunidadesCall.execute();
        return response.body();
    }

    public List<UsuarioModelConfianza> usuario(RequestUsuario requestUsuario) throws IOException {
        GradoDeConfianzaService gradoDeConfianzaService = this.retrofit.create(GradoDeConfianzaService.class);
        Call<List<UsuarioModelConfianza>> usuariosCall = gradoDeConfianzaService.usuario(requestUsuario);
        Response<List<UsuarioModelConfianza>> usuariosResponse = usuariosCall.execute();
        return usuariosResponse.body();
    }
}
