package tp.controllers.usuario;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import tp.dto.UsuarioDetalleResponse;
import tp.dto.UsuariosPageResponse;
import tp.services.usuarios.AdministracionUsuariosService;

@RestController
@RequestMapping("/usuarios")
public class AdministracionUsuariosController {

    private AdministracionUsuariosService administracionUsuariosService;

    public AdministracionUsuariosController(AdministracionUsuariosService administracionUsuariosService) {
        this.administracionUsuariosService = administracionUsuariosService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UsuariosPageResponse listar(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String rol,
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int tamano
    ){
        return this.administracionUsuariosService.listarUsuarios(texto,rol,pagina,tamano);
    }

    @GetMapping("/{keycloakId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioDetalleResponse detalle(@PathVariable String keycloakId){
        return administracionUsuariosService.detalleUsuario(keycloakId);
    }
}
