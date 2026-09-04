package tp.models.entities.converters;

import tp.models.entities.notificador.EstrategiaDeNotificacion;
import tp.models.entities.notificador.mail.EstrategiaDeMail;
import tp.models.entities.notificador.wpp.EstrategiaDeWPP;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstrategiaDeNotificacionConverter implements AttributeConverter<EstrategiaDeNotificacion, String> {
  @Override
  public String convertToDatabaseColumn(EstrategiaDeNotificacion estrategiaDeNotificacion) {
    return estrategiaDeNotificacion == null ? null : estrategiaDeNotificacion.getClass().getName();
  }

  @Override
  public EstrategiaDeNotificacion convertToEntityAttribute(String s) {
    EstrategiaDeNotificacion estrategiaDeNotificacion = null;
    if (s == null)
      return null;
    else
      switch (s) {
        case "tp.models.entities.notificador.wpp.EstrategiaDeWPP" : estrategiaDeNotificacion = new EstrategiaDeWPP(); break;
        case "tp.models.entities.notificador.mail.EstrategiaDeMail" : estrategiaDeNotificacion = new EstrategiaDeMail(); break;
        default: estrategiaDeNotificacion = new EstrategiaDeMail();
    }
    return estrategiaDeNotificacion;
  }
}
