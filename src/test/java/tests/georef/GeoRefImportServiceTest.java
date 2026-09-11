package tests.georef;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tp.repositories.RepositorioLocalidades;
import tp.repositories.RepositorioMunicipios;
import tp.repositories.RepositorioProvincias;
import tp.services.georef.GeoRefImportService;
import tp.services.georef.ListaLocalidades;
import tp.services.georef.ListaMunicipios;
import tp.services.georef.ListaProvincias;
import tp.services.georef.Municipio;
import tp.services.georef.Provincia;
import tp.services.georef.ServicioGeoRef;

@ExtendWith(MockitoExtension.class)
public class GeoRefImportServiceTest {

    @Mock
    RepositorioProvincias repositorioProvincias;
    @Mock
    RepositorioMunicipios repositorioMuncipios;
    @Mock
    RepositorioLocalidades repositorioLocalidades;
    @Mock
    ServicioGeoRef servicioGeoRef;

    @InjectMocks
    GeoRefImportService geoRefImportService;

    private Provincia provincia(Long id, String nombre) {
        Provincia provincia = new Provincia();
        provincia.id = id;
        provincia.nombre = nombre;

        return provincia;
    }

    @Test
    void catalogoYaCargado_siHayProvincias() {
        when(repositorioProvincias.count()).thenReturn(24L);
        assertThat(geoRefImportService.yaCargado()).isEqualTo(true);
    }

    @Test
    void importarCatalogo_reemplazaLaProvinciaSueltaPorLaProvinciaPersistida() throws IOException {
        ListaProvincias listaProvincias = new ListaProvincias();
        listaProvincias.provincias = List.of(provincia(6L, "Buenos Aires"));

        when(servicioGeoRef.listaProvincias()).thenReturn(listaProvincias);

        Municipio laPlata = new Municipio();
        laPlata.id = 100L;
        laPlata.nombre = "La Plata";
        laPlata.provincia = provincia(6L, "Buenos Aires"); // objeto suelto que trae el JSON

        ListaMunicipios listaMunicipios = new ListaMunicipios();
        listaMunicipios.municipios = List.of(laPlata);
        listaMunicipios.total = 1;
        when(servicioGeoRef.municipios()).thenReturn(listaMunicipios);

        ListaLocalidades listaLocalidades = new ListaLocalidades();
        listaLocalidades.localidades = List.of();
        when(servicioGeoRef.localidades()).thenReturn(listaLocalidades);

        Provincia provinciaManaged = provincia(6L, "Buenos Aires");
        when(repositorioProvincias.getReferenceById(6L)).thenReturn(provinciaManaged);

        geoRefImportService.importarCatalogo();

        verify(repositorioProvincias).saveAll(listaProvincias.provincias);
        assertThat(laPlata.provincia).isSameAs(provinciaManaged);
        verify(repositorioMuncipios).saveAll(listaMunicipios.municipios);

    }

}
