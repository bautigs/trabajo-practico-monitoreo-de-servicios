package tp.controllers.ubicacion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import tp.repositories.RepositorioLocalidades;
import tp.repositories.RepositorioMunicipios;
import tp.repositories.RepositorioProvincias;
import tp.server.security.SecurityConfig;
import tp.services.georef.Localidad;
import tp.services.georef.Municipio;
import tp.services.georef.Provincia;

@WebMvcTest(UbicacionController.class)
// se requiere para inyectar en el contexto el bean de cadena de filtros
// para que se utilice en estos slice de test
@Import(SecurityConfig.class)
public class UbicacionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RepositorioProvincias repositorioProvincias;
    @MockBean
    RepositorioMunicipios repositorioMunicipios;
    @MockBean
    RepositorioLocalidades repositorioLocalidades;
    @MockBean
    JwtDecoder jwtDecoder;

    private Provincia provincia(Long id, String nombre) {
        Provincia provincia = new Provincia();
        provincia.id = id;
        provincia.nombre = nombre;
        return provincia;
    }

    @Test
    void provincias_devuelveElListadoOrdenado() throws Exception {
        when(repositorioProvincias.findAll(any(Sort.class))).thenReturn(List.of(provincia(6L, "Buenos Aires")));

        mockMvc.perform(get("/ubicaciones/provincias").with(jwt())).andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(6)).andExpect(jsonPath("$.[0].nombre").value("Buenos Aires"));
    }

    @Test
    void municipio_conProvinciaExistente_devuelveElListado() throws Exception {
        Municipio municipio = new Municipio();
        municipio.id = 100L;
        municipio.nombre = "La Plata";

        when(repositorioProvincias.existsById(6L)).thenReturn(true);
        when(repositorioMunicipios.findByProvinciaIdOrderByNombreAsc(6L)).thenReturn(List.of(municipio));

        mockMvc.perform(get("/ubicaciones/provincias/6/municipios").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].nombre").value("La Plata"));
    }

    @Test
    void municipio_conProvinciaInexistente_devuelve404() throws Exception {
        when(repositorioProvincias.existsById(999L)).thenReturn(false);

        mockMvc.perform(get("/ubicaciones/provincias/999/municipios").with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void localidad_conMunicipioExistente_devuelveElListado() throws Exception {
        Localidad localidad = new Localidad();
        localidad.id = 1000L;
        localidad.nombre = "Centro";

        when(repositorioMunicipios.existsById(100L)).thenReturn(true);
        when(repositorioLocalidades.findByMunicipioIdOrderByNombreAsc(100L)).thenReturn(List.of(localidad));

        mockMvc.perform(get("/ubicaciones/municipios/100/localidades").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1000))
                .andExpect(jsonPath("$.[0].nombre").value("Centro"));
    }

    @Test
    void localidad_conMunicipioInexistente_devuelve404() throws Exception {
        when(repositorioMunicipios.existsById(999L)).thenReturn(false);

        mockMvc.perform(get("/ubicaciones/municipios/999/localidades").with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void sinToken_devuelve401() throws Exception {
        mockMvc.perform(get("/ubicaciones/provincias"))
                .andExpect(status().isUnauthorized());
    }

}
