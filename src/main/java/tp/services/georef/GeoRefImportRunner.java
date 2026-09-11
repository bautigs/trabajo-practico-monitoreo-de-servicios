package tp.services.georef;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// ApplicationRunner: Interfaz funcional utilizada para correr cierto bloque de codigo
// especifico inmediatamente despues de que el application context se haya inicializado pero
// antes que el startup de la aplicacion se haya completado 
@Component
public class GeoRefImportRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(GeoRefImportRunner.class);

    private final GeoRefImportService importService;

    public GeoRefImportRunner(GeoRefImportService importService) {
        this.importService = importService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (importService.yaCargado()) {
            log.info("Catalogo geografico ya cargado, no se reimporta");
            return;
        }

        try {
            importService.importarCatalogo();
        } catch (IOException | RuntimeException e) {
            log.error(
                    "No se pudo importar el catalogo geografico desde GeoRef. La app sigue arrancando; /ubicaciones va a devolver listas vacias hasta el proximo reinicio de red",
                    e);
        }
    }

}
