package tp.controllers.usuario;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PerfilController {

  @GetMapping("/perfil")
  public Map<String, Object> perfil(JwtAuthenticationToken authentication) {
    Jwt jwt = authentication.getToken();
    List<String> roles = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    return Map.of(
        "sub", jwt.getSubject(),
        "username", jwt.getClaimAsString("preferred_username"),
        "roles", roles);
  }
}
