package tp.models.notificador;

import tp.models.notificador.mail.EstrategiaDeMail;
import tp.models.notificador.wpp.EstrategiaDeWPP;

public class Factory {

    public static EstrategiaDeNotificacion crear(String tipo) throws NoExisteFormatoException {
        EstrategiaDeNotificacion estrategiaDeNotificacion = null;
        if (tipo != null)
            switch (tipo) {
                case "MAIL" : estrategiaDeNotificacion = new EstrategiaDeMail(); break;
                case "WHATSAPP" : estrategiaDeNotificacion = new EstrategiaDeWPP(); break;
                default: throw new NoExisteFormatoException();
            }

        return estrategiaDeNotificacion;
    }

}
