package tp.models.notificador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notificacion {

    private String asunto;

    private String cuerpo;

    private String mailDeContacto;

    private String numeroDeContacto;

    private EstrategiaDeNotificacion estrategiaDeNotificacion;
}
