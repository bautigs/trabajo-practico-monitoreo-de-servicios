/*
package tp.models.entities.persistencia;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import tp.models.entities.services.georef.*;
import tp.models.repositories.RepositorioLocalidades;
import tp.models.repositories.RepositorioMunicipios;
import tp.models.repositories.RepositorioProvincias;

import java.io.IOException;

public class Runner {

  public static void main(String[] args) throws IOException {
    new Runner().getLocalidades();
  }

  private void start() {
    entityManager();
  }

  private void getProvincias() throws IOException {
    RepositorioProvincias repositorioProvincias = RepositorioProvincias.getInstancia();

    ServicioGeoRef servicioGeoRef = ServicioGeoRef.getInstancia();
    ListaProvincias listaProvincias = servicioGeoRef.listaProvincias();

    for (Provincia provincia : listaProvincias.provincias) {
      repositorioProvincias.registrar(provincia);
    }
  }

  private void getMunicipios() throws IOException {
    RepositorioMunicipios repositorioMunicipios = RepositorioMunicipios.getInstancia();

    ServicioGeoRef servicioGeoRef = ServicioGeoRef.getInstancia();
    ListaMunicipios listaMunicipios = servicioGeoRef.municipios();

    for (Municipio municipio : listaMunicipios.municipios) {
      repositorioMunicipios.registrar(municipio);
    }
  }

  private void getLocalidades() throws IOException {
    RepositorioLocalidades repositorioLocalidades = RepositorioLocalidades.getInstancia();

    ServicioGeoRef servicioGeoRef = ServicioGeoRef.getInstancia();
    ListaLocalidades listaLocalidades = servicioGeoRef.localidades();

    for (Localidad localidad : listaLocalidades.localidades) {
      if (localidad.getMunicipio().getId() != null) {
        repositorioLocalidades.registrar(localidad);
      }
    }
    System.exit(0);
  }
}
*/
