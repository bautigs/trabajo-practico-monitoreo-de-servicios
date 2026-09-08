package tp.server.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configura la app como un OAuth2 Resource Server: valida los JWT emitidos por Keycloak
 * (issuer configurado en application.yml vía KEYCLOAK_ISSUER_URI) y mapea el claim
 * "realm_access.roles" del token a roles de Spring Security, para poder usar
 * hasRole(...)/@PreAuthorize con los mismos nombres definidos en el realm de Keycloak
 * (ADMIN, BASICO, RESPONSABLE).
 *
 * No hay sesión ni cookies: cada request se autentica solo con el header
 * "Authorization: Bearer &lt;token&gt;".
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sesiones -> sesiones.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

    return http.build();
  }

  private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(this::extraerAuthorities);
    return converter;
  }

  private Collection<GrantedAuthority> extraerAuthorities(Jwt jwt) {
    Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
    if (realmAccess == null || !(realmAccess.get("roles") instanceof List<?> roles)) {
      return List.of();
    }

    return roles.stream()
        .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol))
        .collect(Collectors.toUnmodifiableList());
  }
}
