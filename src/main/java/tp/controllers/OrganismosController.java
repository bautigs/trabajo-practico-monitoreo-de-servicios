package tp.controllers;

import io.javalin.http.Context;
import org.apache.commons.io.FileUtils;
import tp.models.entities.csv.ImportadorCSV;
import tp.models.entities.entidad.Empresa;
import tp.models.entities.entidad.Entidad;
import tp.models.entities.entidad.OrganismoDeControl;
import tp.models.repositories.*;
import tp.server.utils.ICrudViewsHandler;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class OrganismosController implements ICrudViewsHandler {
    public OrganismosController() {

    }

    @Override
    public void create(Context context) {
        HashMap<String, List> model = new HashMap<>();
        model.put("tipos", RepositorioTiposDeEntidad.getInstancia().all());
        context.render("cargaMasiva.hbs", model);
    }

    @Override
    public void save(io.javalin.http.Context context) throws IOException {
        try {

            String workingDir = System.getProperty("user.dir");
            String destinationPath = workingDir + "/src/main/resources/public/archivos/" + context.uploadedFile("fileinfo").filename();
            FileUtils.copyInputStreamToFile(context.uploadedFile("fileinfo").content(), new File(destinationPath));

            // Verifica que el archivo existe en la nueva ubicación
            File file = new File(destinationPath);
            if (file.exists()) {
                System.out.println("Archivo copiado exitosamente a: " + destinationPath);
                String tipoOrganismo = context.formParam("tipo-organismo");
                if (tipoOrganismo.equals("Entidad prestadora")) {
                    System.out.println(context.uploadedFile("fileinfo").filename());
                     List<Empresa> empresas = ImportadorCSV.getInstancia().importarCSVEmpresa(destinationPath);

                     for(Empresa empresa : empresas){
                         List<Entidad> entidades = empresa.getListaDeEntidades();
                         entidades.forEach(entidad -> RepositorioEntidades.getInstancia().registrar(entidad));
                         RepositorioEmpresas.getInstancia().registrar(empresa);
                     }

                     empresas.forEach(empresa -> RepositorioEmpresas.getInstancia().registrar(empresa));

                } else {
                    System.out.println(context.uploadedFile("fileinfo").filename());
                    List<OrganismoDeControl> organismo = ImportadorCSV.getInstancia().importarCSVOrganismos(destinationPath);
                    organismo.forEach(organismoDeControl -> RepositorioTiposDeServicio.getInstancia()
                            .registrar(organismoDeControl.getServicioQueRegula()));
                    organismo.forEach(organismoDeControl -> RepositorioOrganismosDeControl.getInstancia()
                            .registrar(organismoDeControl));

                }
                context.redirect("/carga-masiva");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        context.redirect("/carga-masiva");

    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {

    }

    @Override
    public void index(Context context) {
        context.render("cargaMasiva.hbs");
    }

    public void descargarEjemplo(Context context) {
        try {
            String ejemploFilePath = "/ruta/del/archivo_ejemplo.csv"; // Ruta del archivo de ejemplo
            File file = new File(ejemploFilePath);

            context.contentType("text/csv");
            context.header("Content-Disposition", "attachment; filename=archivo_ejemplo.csv");

            try (InputStream inputStream = new FileInputStream(file);
                 OutputStream outputStream = context.res().getOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al descargar el archivo de ejemplo", e);
        }
    }
}

