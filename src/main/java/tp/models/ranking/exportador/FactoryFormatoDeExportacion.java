package tp.models.ranking.exportador;

public class FactoryFormatoDeExportacion {

    public static EstrategiaDeExportacion crear(String formato, String nombreDelArchivo) {
        EstrategiaDeExportacion estrategiaDeExportacion;
        switch (formato) {
            case "PDF": estrategiaDeExportacion = new ExportarAPDF(nombreDelArchivo); break;
            case "EXCEL": estrategiaDeExportacion = new ExportarAExcel(nombreDelArchivo); break;
            default: throw new NoExisteFormatoException();
        }

        return estrategiaDeExportacion;
    }
}
