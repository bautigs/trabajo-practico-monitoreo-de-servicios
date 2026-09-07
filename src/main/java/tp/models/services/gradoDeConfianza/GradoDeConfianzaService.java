package tp.models.services.gradoDeConfianza;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

import java.util.List;

public interface GradoDeConfianzaService {

    @PUT("servicio/comunidad")
    Call<List<ComunidadModelConfianza>> comunidad(@Body RequestComunidad comunidad);

    @PUT("servicio/usuario")
    Call<List<UsuarioModelConfianza>> usuario(@Body RequestUsuario requestUsuario);
}
