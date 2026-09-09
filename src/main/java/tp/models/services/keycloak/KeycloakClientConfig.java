package tp.models.services.keycloak;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration 
public class KeycloakClientConfig {
    @Bean 
    public RestClient keycloakRestClient(KeycloakAdminProperties properties){
        return RestClient.builder()
                    .baseUrl(properties.serverUrl())
                    .build();
    }
}
