package tp.services.georef;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeoRefClientConfig {
    @Bean
    public ServicioGeoRef servicioGeoRef() {
        return ServicioGeoRef.getInstancia();
    }
}
