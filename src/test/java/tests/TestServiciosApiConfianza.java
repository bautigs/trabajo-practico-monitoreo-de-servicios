package tests;

import tp.models.services.gradoDeConfianza.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestServiciosApiConfianza {
    public static void main(String[] args) throws IOException {
        UsuarioModelConfianza u1 = UsuarioModelConfianza.builder().id(1L).puntosDeConfianza(1.9).gradoDeConfianza("NoConfiable").activo(false).build();
        ComunidadModelConfianza c1 = ComunidadModelConfianza.builder().id(1L).usuarios(List.of(u1)).gradoDeConfianza("ConReservas").activo(true).puntosDeConfianza(2.0).build();

        List<ComunidadModelConfianza> listaComunidades = new ArrayList<>();
        listaComunidades.add(c1);

        RequestComunidad lc = RequestComunidad.builder().comunidades(listaComunidades).build();

        ServicioGradoDeConfianza servicioGradoDeConfianza = ServicioGradoDeConfianza.getInstancia();
        List<ComunidadModelConfianza> comunidades = servicioGradoDeConfianza.comunidad(lc);

        System.out.println(comunidades.get(0).getGradoDeConfianza());

        UsuarioModelConfianza u2 = UsuarioModelConfianza.builder().id(1L).puntosDeConfianza(2.0).gradoDeConfianza("ConReservas").activo(true).build();
        List<UsuarioModelConfianza> listaUsuarios = new ArrayList<>();
        listaUsuarios.add(u2);

        IncidenteModelConfianza i1 = IncidenteModelConfianza.builder().id(1l).lugar("Villa Crespo").idUsuAp(1L).idUsuCi(1L).horaApertura("2023-09-18T15:25:15.00").horaCierre("2023-09-18T20:26:15.00").activo(true).build();
        List<IncidenteModelConfianza> listaIncidentes = new ArrayList<>();
        listaIncidentes.add(i1);
        RequestUsuario requestUsuario = RequestUsuario.builder().usuarioModelConfianzas(listaUsuarios).incidentes(listaIncidentes).build();

        List<UsuarioModelConfianza> respuestaServicio = servicioGradoDeConfianza.usuario(requestUsuario);
        System.out.println(respuestaServicio.get(0).getPuntosDeConfianza());

    }
}
