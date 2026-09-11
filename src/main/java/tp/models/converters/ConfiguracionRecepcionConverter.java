package tp.models.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tp.models.comunidad.Asincronico;
import tp.models.comunidad.ConfiguracionRecepcion;
import tp.models.comunidad.Sincronico;

@Converter(autoApply = true)
public class ConfiguracionRecepcionConverter
    implements AttributeConverter<ConfiguracionRecepcion, String> {

  @Override
  public String convertToDatabaseColumn(ConfiguracionRecepcion configuracion) {
    if (configuracion instanceof Sincronico) {
      return "SINCRONICO";
    }
    if (configuracion instanceof Asincronico) {
      return "ASINCRONICO";
    }
    return null;
  }

  @Override
  public ConfiguracionRecepcion convertToEntityAttribute(String valor) {
    if (valor == null) {
      return null;
    }
    return switch (valor) {
      case "SINCRONICO" -> new Sincronico();
      case "ASINCRONICO" -> new Asincronico();
      default -> null;
    };
  }
}