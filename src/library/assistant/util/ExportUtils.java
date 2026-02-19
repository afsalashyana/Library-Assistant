package library.assistant.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class ExportUtils {

    public static void writeToCSV(TableView<?> tableView, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            exportData(tableView, file);
        }
    }

    private static void exportData(TableView<?> tableView, File file) {
        try (Writer writer = new FileWriter(file);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            // Write Header
            List<String> headers = new java.util.ArrayList<>();
            tableView.getColumns().forEach(column -> headers.add(column.getText()));
            csvPrinter.printRecord(headers);

            // Write Data
            for (Object item : tableView.getItems()) {
                List<Object> rowData = new java.util.ArrayList<>();
                for (javafx.scene.control.TableColumn<?, ?> column : tableView.getColumns()) {
                    // This assumes the cell value factory returns an observable value that we can get the value from
                    // However, we might need a more robust way to extract data depending on how the TableView is set up.
                    // For now, let's try to get the cell observable value. 
                    // But standard way without reflection or complex casting is tricky if we don't know the type T.
                    // A common workaround is to use the cell data if possible.
                    // Let's assume the models (Book/Member) have valid toString() or we can rely on PropertyValueFactory.
                    
                    if (column.getCellData(tableView.getItems().indexOf(item)) != null) {
                         rowData.add(column.getCellData(tableView.getItems().indexOf(item)).toString());
                    } else {
                         rowData.add("");
                    }
                }
                csvPrinter.printRecord(rowData);
            }

            csvPrinter.flush();
            AlertMaker.showMaterialDialog(null, null, new java.util.ArrayList<>(), "Success", "Data exported successfully to " + file.getAbsolutePath());
        } catch (IOException e) {
            AlertMaker.showMaterialDialog(null, null, new java.util.ArrayList<>(), "Error", "Could not save data to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
