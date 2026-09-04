package tp.models.entities.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import tp.models.entities.entidad.Empresa;
import tp.models.entities.entidad.Entidad;
import tp.models.entities.entidad.OrganismoDeControl;
import tp.models.entities.entidad.TipoEntidad;
import tp.models.entities.servicios.TipoServicio;
import tp.models.repositories.RepositorioTiposDeEntidad;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ImportadorCSV {

    private static ImportadorCSV instancia = null;

    public static ImportadorCSV getInstancia() {
        if (instancia == null) {
            instancia = new ImportadorCSV();
        }
        return instancia;
    }
//TODO agregar builders de Empresas, Organismos de control y Entidades
    public List<OrganismoDeControl> importarCSVOrganismos(String ruta) throws IOException {
        List<OrganismoDeControl> lista = new ArrayList<>();
        try {
            Reader reader = new FileReader(ruta);
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

            for (CSVRecord registro : parser) {
                // registro -> lista de string

                int max = registro.size();
                int i = 0;

                while(i < max){
                    OrganismoDeControl org = new OrganismoDeControl();
                    String nombreOrganismoControl = registro.get(i);
                    String servicioPresta = registro.get(i+1);
                    org.setNombre(nombreOrganismoControl);
                    TipoServicio tipoServicio = new TipoServicio();
                    tipoServicio.setNombre(servicioPresta);
                    org.setServicioQueRegula(tipoServicio);
                    lista.add(org);
                    i+=2;
                }

            }



        } catch (IOException e ){
            System.out.println("Error: " + e.getMessage());
        }

        return lista;
    }

    public List<Empresa> importarCSVEmpresa(String ruta) throws IOException {
        List<Empresa> lista = new ArrayList<>();
        try {
            Reader reader = new FileReader(ruta);
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

            for (CSVRecord registro : parser) {
                // registro -> lista de string

                int max = registro.size();
                Empresa empresa = new Empresa();
                empresa.setNombre(registro.get(0));
                System.out.println(registro.get(0));

                int i = 1;

                while(i < max){
                    String nombreEntidad = registro.get(i);
                    String nombreTipoEntidad  = registro.get(i+1);
                    TipoEntidad tipo = RepositorioTiposDeEntidad.getInstancia().findByNombre(nombreTipoEntidad);
                    System.out.println(nombreEntidad);
                    Entidad e = new Entidad();
                    e.setNombre(nombreEntidad);
                    e.setTipoEntidad(tipo);
                    empresa.agregarEntidades(e);
                    i+=2;
                }

                lista.add(empresa);
            }



        } catch (IOException e ){
            System.out.println("Error: " + e.getMessage());
        }

        return lista;
    }

}