package tp.controllers.ubicacion;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tp.repositories.RepositorioProvincias;
import tp.repositories.RepositorioMunicipios;
import tp.repositories.RepositorioLocalidades;
import tp.dto.ProvinciaDTO;
import tp.dto.MunicipioDTO;
import tp.dto.LocalidadDTO;
import tp.server.exceptions.RecursoNoEncontradoException;

@RestController
@RequestMapping("/ubicaciones")
public class UbicacionController {
    private final RepositorioProvincias repositorioProvincias;
    private final RepositorioMunicipios repositorioMunicipios;
    private final RepositorioLocalidades repositorioLocalidades;

    public UbicacionController(RepositorioProvincias repositorioProvincias, RepositorioMunicipios repositorioMunicipios,
            RepositorioLocalidades repositorioLocalidades) {
        this.repositorioProvincias = repositorioProvincias;
        this.repositorioMunicipios = repositorioMunicipios;
        this.repositorioLocalidades = repositorioLocalidades;
    }

    @GetMapping("/provincias")
    public List<ProvinciaDTO> getProvincias() {
        return this.repositorioProvincias.findAll(Sort.by("nombre")).stream()
                .map(p -> new ProvinciaDTO(p.id, p.nombre)).toList();
    }

    @GetMapping("/provincias/{id}/municipios")
    public List<MunicipioDTO> getMuncipiosPorProvincia(@PathVariable Long id) {

        if (!this.repositorioProvincias.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe la provincia " + id);
        }

        return this.repositorioMunicipios.findByProvinciaIdOrderByNombreAsc(id).stream()
                .map(m -> new MunicipioDTO(m.id, m.nombre)).toList();
    }

    @GetMapping("/municipios/{id}/localidades")
    public List<LocalidadDTO> getLocalidadesPorMunicipio(@PathVariable Long id) {
        if (!this.repositorioMunicipios.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe el municipio " + id);
        }

        return this.repositorioLocalidades.findByMunicipioIdOrderByNombreAsc(id).stream()
                .map(l -> new LocalidadDTO(l.id, l.nombre)).toList();
    }

}
