package tp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import tp.services.keycloak.KeycloakAdminProperties;

import java.io.IOException;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "tp")
@EnableJpaRepositories(basePackages = "tp.repositories")
@EntityScan(basePackages = { "tp.models", "tp.services.georef" })
@EnableConfigurationProperties(KeycloakAdminProperties.class)
@EnableScheduling
public class App {
  public static void main(String[] args) throws IOException {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(App.class, args);
  }
}
