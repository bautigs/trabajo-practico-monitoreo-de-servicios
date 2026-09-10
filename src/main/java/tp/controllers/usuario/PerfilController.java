package tp.controllers.usuario;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tp.services.usuarios.PerfilService;
import tp.dto.PerfilResponse;

@RestController
public class PerfilController {

  private final PerfilService perfilService;

  public PerfilController(PerfilService perfilService) {
    this.perfilService = perfilService;
  }

  @GetMapping("/perfil")
  public PerfilResponse perfil(JwtAuthenticationToken authentication) {
    List<String> roles = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .map(rol -> rol.startsWith("ROLE_") ? rol.substring(5) : rol)
        .toList();

    return perfilService.perfilActual(authentication.getToken(), roles);
  }

}
