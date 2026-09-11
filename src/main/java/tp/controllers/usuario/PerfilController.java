package tp.controllers.usuario;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tp.services.usuarios.PerfilService;
import tp.dto.ActualizarPerfilRequest;
import tp.dto.PerfilResponse;
import tp.dto.UbicacionRequest;

@RestController
public class PerfilController {

  private final PerfilService perfilService;

  public PerfilController(PerfilService perfilService) {
    this.perfilService = perfilService;
  }

  @GetMapping("/perfil")
  public PerfilResponse perfil(JwtAuthenticationToken authentication) {
    List<String> roles = roles(authentication);

    return perfilService.perfilActual(authentication.getToken(), roles);
  }

  @PutMapping("/perfil")
  public PerfilResponse actualizarPerfil(JwtAuthenticationToken authentication,
      @Valid @RequestBody ActualizarPerfilRequest request) {
    return perfilService.actualizarPerfil(authentication.getToken(), roles(authentication), request);
  }

  @PutMapping("/perfil/ubicacion")
  public PerfilResponse actualizarUbicacion(JwtAuthenticationToken authentication,
      @Valid @RequestBody UbicacionRequest request) {
    return perfilService.actualizarUbicacion(authentication.getToken(), roles(authentication), request);
  }

  private List<String> roles(JwtAuthenticationToken authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .map(rol -> rol.startsWith("ROLE_") ? rol.substring(5) : rol)
        .toList();
  }

}
