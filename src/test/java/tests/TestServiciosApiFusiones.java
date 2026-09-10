package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tp.services.fusionDeComunidades.*;

public class TestServiciosApiFusiones {

        public static void main(String[] args) throws IOException {

                ServicioFusionDeComunidades servicioFusionDeComunidades = ServicioFusionDeComunidades.getInstancia();

                MiembroModel m1 = MiembroModel.builder().id(1L).nombre("string").build();
                EstablecimientoModel e1 = EstablecimientoModel.builder().id(1L).nombre("string").build();
                ServicioModel s1 = ServicioModel.builder().id(1L).nombre("string").build();

                ComunidadModel c1 = ComunidadModel.builder().id(3L).nombre("string").miembros(List.of(m1))
                                .establecimientosObservados(List.of(e1))
                                .serviciosObservados(List.of(s1)).gradoDeConfianza("Alto").build();

                ComunidadModel c2 = ComunidadModel.builder().id(1L).nombre("Com")
                                .miembros(List.of(m1))
                                .serviciosObservados(List.of(s1))
                                .establecimientosObservados(List.of(e1))
                                .gradoDeConfianza("Alto")
                                .build();

                List<PropuestaDeFusionModel> propuestaDeFusionModel = new ArrayList<>();
                List<ComunidadModel> comunidades = new ArrayList<>();
                comunidades.add(c1);
                comunidades.add(c2);
                ArrayList<PropuestaDeFusionModel> propuestas = servicioFusionDeComunidades.sugerirFusiones(
                                PeticionModel.builder()
                                                .propuestas(propuestaDeFusionModel)
                                                .comunidades(comunidades)
                                                .build());

                System.out.println(propuestas.get(0).fechaPropuesta);

                ComunidadModel comFusionada = servicioFusionDeComunidades.fusionarComunidades(propuestas.get(0));
                System.out.println(comFusionada.getNombre());

        }
}
