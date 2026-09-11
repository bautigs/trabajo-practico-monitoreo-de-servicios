package tp.services.georef;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tp.repositories.RepositorioProvincias;
import tp.repositories.RepositorioMunicipios;
import tp.repositories.RepositorioLocalidades;

@Service
public class GeoRefImportService {
    private static final Logger log = LoggerFactory.getLogger(GeoRefImportService.class);

    private final RepositorioProvincias repositorioProvincias;
    private final RepositorioMunicipios repositorioMunicipios;
    private final RepositorioLocalidades repositorioLocalidades;
    private final ServicioGeoRef servicioGeoref;

    public GeoRefImportService(RepositorioProvincias repositorioProvincias, RepositorioMunicipios repositorioMunicipios,
            RepositorioLocalidades repositorioLocalidades, ServicioGeoRef servicioGeoref) {
        this.repositorioProvincias = repositorioProvincias;
        this.repositorioMunicipios = repositorioMunicipios;
        this.repositorioLocalidades = repositorioLocalidades;
        this.servicioGeoref = servicioGeoref;
    }

    public boolean yaCargado() {
        return this.repositorioProvincias.count() > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void importarCatalogo() throws IOException {
        List<Provincia> provincias = servicioGeoref.listaProvincias().provincias;

        repositorioProvincias.saveAll(provincias);

        ListaMunicipios listaMunicipios = servicioGeoref.municipios();

        if (listaMunicipios.total > listaMunicipios.municipios.size()) {
            log.warn("Georef devolvio {} de {} municipios; subi el max en ServicioGeoref.municipios()",
                    listaMunicipios.municipios.size(), listaMunicipios.total);
        }

        // el JSON trae un objeto "provincia" suelto (deserializado por Gson, no
        // persistido); lo reemplazo por una referencia a la fila que YA guardamos,
        // si no Hibernate no sabe si es un objeto nuevo o existente al hacer el INSERT.
        // se usa getReferenceById en vez de findById pues findById trae todos los
        // datos, en cambio
        // getReferenceById trae un objeto proxy con id. Dicho proxy generará una
        // consulta a la base solo
        // si se acceden a datos
        List<Municipio> municipios = listaMunicipios.municipios;
        municipios.forEach(m -> m.provincia = repositorioProvincias.getReferenceById(m.provincia.id));
        repositorioMunicipios.saveAll(municipios);

        ListaLocalidades listaLocalidades = servicioGeoref.localidades();

        if (listaLocalidades.total != null && listaLocalidades.total > listaLocalidades.localidades.size()) {
            log.warn("GeoRef devolvio {} de {} localidades; subi el max en ServicioGeoRef.localidades()",
                    listaLocalidades.localidades.size(), listaLocalidades.total);
        }

        List<Localidad> localidades = listaLocalidades.localidades;

        localidades.forEach(
                l -> l.municipio = l.municipio == null ? null : repositorioMunicipios.getReferenceById(l.municipio.id));

        repositorioLocalidades.saveAll(localidades);

        log.info("Catalogo geografico importado: {} provincias, {} municipios, {} localidades", provincias.size(),
                municipios.size(), localidades.size());
    }

}
