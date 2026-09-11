package tp.models.comunidad;

import lombok.Getter;

import java.util.*;


public class ConfiguracionDiferida {

    private static ConfiguracionDiferida instancia = null;

    public static ConfiguracionDiferida getInstancia(){
        if(instancia == null){
            instancia = new ConfiguracionDiferida();
        }
        return instancia;
    }
    private Timer timer;
    @Getter
    private static List<Asincronico> asincronicos = new ArrayList<>();

    public ConfiguracionDiferida() {
        timer = new Timer();
        this.programarNotificacionesHorario();
    }

    public static void agregarAsincronico(Asincronico asincronico) {
        asincronicos.add(asincronico);
    }

    public void programarNotificacionesHorario() {
        for (int horario = 0; horario < 24; horario++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, horario);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);


            Date horarioProgramado = calendar.getTime();
            TimerTask task = new ImplementacionTimer(this, horario);
            timer.schedule(task, horarioProgramado, 24 * 60 * 60 * 1000);
        }
    }

    public void enviarNotificacionesHorario(int horario) {
        asincronicos.forEach(asincronico -> asincronico.enviarNotificaciones(horario));
    }
}
