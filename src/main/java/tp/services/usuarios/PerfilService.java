package tp.services.usuarios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tp.dto.PerfilResponse;
import tp.models.comunidad.Persona;
import tp.models.builders.PersonaBuilder;

import tp.repositories.RepositorioPersonas;

@Service
public class PerfilService {

    private final RepositorioPersonas repositorioPersonas;

    private final PerfilMapper mapper;

    public PerfilService(RepositorioPersonas repositorioPersonas, PerfilMapper mapper) {
        this.repositorioPersonas = repositorioPersonas;
        this.mapper = mapper;
    }

    @Transactional
    public PerfilResponse perfilActual(Jwt jwt, List<String> roles) {
        Persona persona = repositorioPersonas.findByKeycloakId(jwt.getSubject()).orElseGet(() -> provisionar(jwt));

        return mapper.toResponse(persona, jwt.getClaimAsString("preferred_username"), roles);
    }

    private Persona provisionar(Jwt jwt) {
        Persona nueva = new PersonaBuilder()
                .keycloakId(jwt.getSubject())
                .mail(jwt.getClaimAsString("email"))
                .nombre(nombreApellido(jwt))
                .fechaDeAlta(LocalDate.now())
                .build();

        try {
            return repositorioPersonas.saveAndFlush(nueva);
        } catch (DataIntegrityViolationException e) {
            return repositorioPersonas.findByKeycloakId(jwt.getSubject()).orElseThrow(() -> e);
        }
    }

    private String nombreApellido(Jwt jwt) {
        String name = jwt.getClaimAsString("name");
        if (name != null && !name.isBlank()) {
            return name;
        }

        String given = jwt.getClaimAsString("given_name");
        String family = jwt.getClaimAsString("family_name");
        return ((given == null ? "" : given) + " " + (family == null ? "" : family)).trim();
    }

}
