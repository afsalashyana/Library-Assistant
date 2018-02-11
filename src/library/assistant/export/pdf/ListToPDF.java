package library.assistant.export.pdf;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.datatable.DataTable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import library.assistant.alert.AlertMaker;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

/*
@author afsal-5502
 */

public class ListToPDF {

    public enum Orientation {
        PORTRAIT, LANDSCAPE
    };

    public boolean doPrintToPdf(List<List> list, File saveLoc, Orientation orientation) {
        try {
            if (saveLoc == null) {
                return false;
            }
            if (!saveLoc.getName().endsWith(".pdf")) {
                saveLoc = new File(saveLoc.getAbsolutePath() + ".pdf");
            }
            //Initialize Document
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            //Create a landscape page
            if (orientation == Orientation.LANDSCAPE) {
                page.setMediaBox(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            } else {
                page.setMediaBox(new PDRectangle(PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight()));
            }

            doc.addPage(page);
            //Initialize table
            float margin = 10;
            float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
            float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
            float yStart = yStartNewPage;
            float bottomMargin = 0;

            BaseTable dataTable = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true,
                    true);
            DataTable t = new DataTable(dataTable, page);
            t.addListToTable(list, DataTable.HASHEADER);
            dataTable.draw();
            doc.save(saveLoc);
            doc.close();

            return true;
        } catch (IOException ex) {
            AlertMaker.showErrorMessage("Error occurred during PDF export", ex.getMessage());
        }
        return false;
    }

}
