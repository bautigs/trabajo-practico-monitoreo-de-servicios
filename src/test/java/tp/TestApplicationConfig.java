package tp;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Config de test para los slice tests de Spring (@WebMvcTest, etc.).
 * El @SpringBootApplication real vive en tp.server.App, que no es un paquete
 * ancestro de los tests, asi que Spring no lo encuentra al subir por el arbol.
 * Esta clase, en la raiz `tp`, es la que descubren los tests bajo `tp.*`.
 *
 * El @ComponentScan apunta solo a tp.controllers: alcanza para que @WebMvcTest
 * descubra los controllers, y evita chocar con el otro @SpringBootConfiguration
 * (tp.server.App) y con su @EnableJpaRepositories.
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "tp.controllers")
class TestApplicationConfig {
}
