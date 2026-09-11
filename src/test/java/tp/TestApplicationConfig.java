package tp;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

/**
 * Config de test para los slice tests de Spring (@WebMvcTest, etc.).
 * El @SpringBootApplication real vive en tp.server.App, que no es un paquete
 * ancestro de los tests, asi que Spring no lo encuentra al subir por el arbol.
 * Esta clase, en la raiz `tp`, es la que descubren los tests bajo `tp.*`.
 *
 * El @ComponentScan apunta a tp.controllers y tp.server.exceptions: alcanza
 * para que @WebMvcTest descubra los controllers y el @RestControllerAdvice
 * (GlobalExceptionHandler), y evita chocar con el otro @SpringBootConfiguration
 * (tp.server.App) y con su @EnableJpaRepositories.
 *
 * excludeFilters = TypeExcludeFilter: sin esto, el filtro de "solo el/los
 * controller(s) que declara @WebMvcTest" NO se aplica. Ese filtro es magia
 * de @SpringBootApplication (viene en sus meta-anotaciones); en un
 * @ComponentScan a mano como este hay que pedirlo explicitamente, si no
 * @WebMvcTest(UnController.class) igual instancia TODOS los controllers que
 * encuentre el scan, no solo el indicado.
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"tp.controllers", "tp.server.exceptions"},
    excludeFilters = @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class))
class TestApplicationConfig {
}
