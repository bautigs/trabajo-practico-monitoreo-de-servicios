package tp.models.entities.notificador.wpp;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import tp.models.entities.notificador.EstrategiaDeNotificacion;
import tp.models.entities.notificador.Notificacion;


public class EstrategiaDeWPP implements EstrategiaDeNotificacion {
    final String ACCOUNT_SID = "tu_account_sid";
    final String AUTH_TOKEN = "tu_auth_token";
    @Override
    public void notificar(Notificacion unaNotificacion) {
        String numero = unaNotificacion.getNumeroDeContacto();
        String mensaje = unaNotificacion.getCuerpo();
        enviarMensaje(numero, mensaje);
    }

    public String enviarMensaje(String destinatario, String mensaje) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        final String telefonoEmisor = "Numero de Telefono nuestro";

        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("whatsapp:" + destinatario),
                        new com.twilio.type.PhoneNumber("whatsapp:" + telefonoEmisor),
                        mensaje)
                .create();

        System.out.println("Mensaje de WhatsApp enviado con el SID: " + message.getSid());

        return "OK";
    }

}
