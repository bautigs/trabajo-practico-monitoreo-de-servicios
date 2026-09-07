package tp.models.comunidad;

import java.util.TimerTask;

public class ImplementacionTimer extends TimerTask {

    ConfiguracionDiferida configDiferido;

    int horarioProgramado;
    public void run(){
        configDiferido.enviarNotificacionesHorario(horarioProgramado);
    }

    public ImplementacionTimer(ConfiguracionDiferida unConfigDiferido ,int horario){
        this.configDiferido = unConfigDiferido;
        this.horarioProgramado = horario;
    }
}
