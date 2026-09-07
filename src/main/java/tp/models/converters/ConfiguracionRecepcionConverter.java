package tp.models.converters;

import tp.models.comunidad.Asincronico;
import tp.models.comunidad.ConfiguracionRecepcion;
import tp.models.comunidad.Sincronico;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConfiguracionRecepcionConverter implements AttributeConverter<ConfiguracionRecepcion, String> {

  @Override
  public String convertToDatabaseColumn(ConfiguracionRecepcion configuracionRecepcion) {
    return configuracionRecepcion == null ? null : configuracionRecepcion.getClass().getName();
  }

@Override
public ConfiguracionRecepcion convertToEntityAttribute(String s) {
  if (s == null) {
    return null;
  }
  ConfiguracionRecepcion configuracionRecepcion = null;
  switch (s) {
    case "tp.models.entities.comunidad.Sincronico":
      configuracionRecepcion = new Sincronico(); break;
    case "tp.models.entities.comunidad.Asincornico":
      configuracionRecepcion = new Asincronico(); break;
    default : configuracionRecepcion = null;
  }
  return configuracionRecepcion;
}

}
