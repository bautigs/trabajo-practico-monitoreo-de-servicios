package tp.models.ranking.exportador;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExportarAPDF implements EstrategiaDeExportacion{
    private String nombreDeArchivo;

    public ExportarAPDF(String nombreDeArchivo) {
        this.nombreDeArchivo = nombreDeArchivo;
    }

    public void exportarFormato(Documento exportable) {
        PDDocument doc = new PDDocument();
        PDPage myPage = new PDPage();
        doc.addPage(myPage);
        try {
            PDPageContentStream cont = new PDPageContentStream(doc, myPage);
            cont.beginText();
            cont.setFont(PDType1Font.TIMES_ROMAN, 12);
            cont.setLeading(14.5f);
            cont.newLineAtOffset(25, 700);
            agregarDatos(cont, exportable.getDatos());

            cont.endText();
            cont.close();
            doc.save(rutaCompletaDelArchivo());
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String rutaCompletaDelArchivo(){
        return Config.RUTA_EXPORTACION + this.nombreDeArchivo;
    }

    private void agregarDatos(PDPageContentStream pagina, Map<String, List<String>> datos) throws IOException {
        for (Map.Entry<String, List<String>> entry : datos.entrySet()) {
            pagina.newLine();
            String datosDeLaFila = String.join(", ", entry.getValue());
            pagina.showText(datosDeLaFila);
        }
    }

}
