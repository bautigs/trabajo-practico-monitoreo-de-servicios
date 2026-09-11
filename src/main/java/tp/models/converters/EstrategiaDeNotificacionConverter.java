package tp.models.converters;

import tp.models.notificador.EstrategiaDeNotificacion;
import tp.models.notificador.mail.EstrategiaDeMail;
import tp.models.notificador.wpp.EstrategiaDeWPP;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstrategiaDeNotificacionConverter implements AttributeConverter<EstrategiaDeNotificacion, String> {
  @Override
  public String convertToDatabaseColumn(EstrategiaDeNotificacion estrategiaDeNotificacion) {
    if (estrategiaDeNotificacion instanceof EstrategiaDeWPP) {
      return "WHATSAPP";
    }
    if (estrategiaDeNotificacion instanceof EstrategiaDeMail) {
      return "MAIL";
    }
    return null;
  }

  @Override
  public EstrategiaDeNotificacion convertToEntityAttribute(String valor) {
    if (valor == null) {
      return null;
    }
    return switch (valor) {
      case "MAIL" -> new EstrategiaDeMail();
      case "WHATSAPP" -> new EstrategiaDeWPP();
      default -> null;
    };
  }
}
