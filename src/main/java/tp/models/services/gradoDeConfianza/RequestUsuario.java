package tp.models.services.gradoDeConfianza;

import lombok.Builder;

import java.util.List;
@Builder
public class RequestUsuario {
    private List<UsuarioModelConfianza> usuarioModelConfianzas;
    private List<IncidenteModelConfianza> incidentes;
}
