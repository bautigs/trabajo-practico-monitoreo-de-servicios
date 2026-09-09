package tests.keycloak;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tp.models.services.keycloak.KeycloakAdminService;
import tp.models.services.keycloak.KeycloakUser;

@Tag("integration")
@SpringBootTest 
public class KeycloakAdminServiceIT {

    @Autowired 
    KeycloakAdminService service;

    @Test 
    void listaLosUsuariosDePruebaDelRealm(){
        assertThat(service.listarUsuarios(null, 0,100)).extracting(KeycloakUser::username).contains("admin","basico");
    }
}
