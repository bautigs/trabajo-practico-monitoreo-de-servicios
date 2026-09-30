package tp.dto;

import java.util.List;

public record UsuariosPageResponse(
        List<UsuarioResumenResponse> usuarios,
        int pagina,
        int tamano,
        long total
) {
}
