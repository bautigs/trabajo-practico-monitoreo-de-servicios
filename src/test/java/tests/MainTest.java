package tests;

import tp.models.csv.ImportadorCSV;
import tp.models.entidad.Empresa;
import tp.models.entidad.OrganismoDeControl;

import java.io.IOException;
import java.util.List;

public class MainTest {
  public static void main(String[] args) throws IOException {
      ImportadorCSV prueba = new ImportadorCSV();
      List<OrganismoDeControl> listOrg = prueba.importarCSVOrganismos("");

      for(OrganismoDeControl organsimo : listOrg){
          System.out.println(organsimo.getNombre());

          for(Empresa empresa : organsimo.getListaEmpresasQueRegula()){
              System.out.println(empresa.getNombre());
          }

          System.out.println("/n");
      }
  }
}
