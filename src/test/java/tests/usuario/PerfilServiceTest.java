package tests.usuario;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import tp.repositories.RepositorioPersonas;
import tp.repositories.RepositorioTipoDeServicio;
import tp.server.exceptions.RecursoNoEncontradoException;
import tp.repositories.RepositorioEntidades;
import tp.repositories.RepositorioProvincias;
import tp.repositories.RepositorioMunicipios;
import tp.repositories.RepositorioLocalidades;
import tp.services.usuarios.PerfilService;
import tp.dto.ActualizarPerfilRequest;
import tp.dto.PerfilResponse;
import tp.models.comunidad.Persona;
import tp.models.builders.PersonaBuilder;
import tp.services.usuarios.PerfilMapper;
import tp.services.georef.Provincia;
import tp.dto.UbicacionRequest;

@ExtendWith(MockitoExtension.class)
public class PerfilServiceTest {
    @Mock
    RepositorioPersonas repositorioPersonas;
    @Mock
    RepositorioTipoDeServicio repositorioTiposDeServicio;
    @Mock
    RepositorioEntidades repositorioEntidades;
    @Mock
    RepositorioProvincias repositorioProvincias;
    @Mock
    RepositorioMunicipios repositorioMunicipios;
    @Mock
    RepositorioLocalidades repositorioLocalidades;

    // mapper real (no tiene dependencias): asi el test verifica el mapeo de verdad
    @Spy
    PerfilMapper perfilMapper = new PerfilMapper();

    @InjectMocks
    PerfilService perfilService;

    private Jwt jwt(String subject) {
        return Jwt.withTokenValue("t")
                .header("alg", "none")
                .subject(subject)
                .claim("name", "Ana Perez")
                .claim("email", "ana@x.com")
                .claim("preferred_username", "ana")
                .build();
    }

    @Test
    void siLaPersonaExiste_laDevuelveSinCrearla() {
        Persona existente = new PersonaBuilder().keycloakId("kc-1").nombre("Ana Perez").build();

        when(repositorioPersonas.findByKeycloakId("kc-1")).thenReturn(Optional.of(existente));

        PerfilResponse response = perfilService.perfilActual(jwt("kc-1"), List.of("BASICO"));

        assertThat(response.nombreApellido()).isEqualTo("Ana Perez");
        assertThat(response.roles()).containsExactly("BASICO");

        // verificia que para el mock de repositorio de personas nucna se haya llamado
        // al metodo save and flush
        verify(repositorioPersonas, never()).saveAndFlush(any());
    }

    @Test
    void siNoExiste_provisionarConLosClaimsDelToken() {
        when(repositorioPersonas.findByKeycloakId("kc-2")).thenReturn(Optional.empty());
        when(repositorioPersonas.saveAndFlush(any(Persona.class))).thenAnswer(i -> i.getArgument(0));

        PerfilResponse response = perfilService.perfilActual(jwt("kc-2"), List.of("BASICO"));

        assertThat(response.keycloakId()).isEqualTo("kc-2");
        assertThat(response.mailDeContacto()).isEqualTo("ana@x.com");
        assertThat(response.nombreApellido()).isEqualTo("Ana Perez");
        assertThat(response.fechaDeAlta()).isNotNull();
        verify(repositorioPersonas).saveAndFlush(any(Persona.class));
    }

    @Test
    void actualizarPerfil_soloModificaLosCamposPresentes() {
        Persona persona = new PersonaBuilder()
                .keycloakId("kc-1").nombre("Ana Perez").numero("111").build();
        when(repositorioPersonas.findByKeycloakId("kc-1")).thenReturn(Optional.of(persona));

        ActualizarPerfilRequest request = new ActualizarPerfilRequest(null, "MAIL", null, null, null);

        PerfilResponse response = perfilService.actualizarPerfil(jwt("kc-1"), List.of("BASICO"), request);

        assertThat(persona.getNumeroDeContacto()).isEqualTo("111");
        assertThat(response.estrategiaNotificacion()).isEqualTo("MAIL");
    }

    @Test
    void actualizarUbicacion_conProvinciaValida_laAsigna() {
        Persona persona = new PersonaBuilder().keycloakId("kc-1").nombre("Ana").build();
        Provincia buenosAires = new Provincia();
        buenosAires.id = 6L;

        when(repositorioPersonas.findByKeycloakId("kc-1")).thenReturn(Optional.of(persona));
        when(repositorioProvincias.findById(6L)).thenReturn(Optional.of(buenosAires));

        PerfilResponse response = perfilService.actualizarUbicacion(jwt("kc-1"), List.of("BASICO"),
                new UbicacionRequest(6L, null, null));

        assertThat(response.ubicacion().provinciaId()).isEqualTo(6L);
        assertThat(persona.getProvincia()).isSameAs(buenosAires);
    }

    @Test
    void actualizarUbicacion_provinciaInexistente_lanza404() {
        Persona persona = new PersonaBuilder().keycloakId("kc-1").nombre("Ana").build();
        when(repositorioPersonas.findByKeycloakId("kc-1")).thenReturn(Optional.of(persona));
        when(repositorioProvincias.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> perfilService.actualizarUbicacion(
                jwt("kc-1"), List.of("BASICO"), new UbicacionRequest(999L, null, null)))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }
}
