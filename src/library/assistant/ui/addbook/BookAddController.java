package library.assistant.ui.addbook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.data.wrapper.Book;
import library.assistant.database.DataHelper;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listbook.BookListController;

public class BookAddController implements Initializable {

    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField publisher;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    DatabaseHandler databaseHandler;
    @FXML
    private StackPane rootPane;
    private Boolean isInEditMode = Boolean.FALSE;
    @FXML
    private AnchorPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
    }

    @FXML
    private void addBook(ActionEvent event) {
        String bookID = id.getText();
        String bookAuthor = author.getText();
        String bookName = title.getText();
        String bookPublisher = publisher.getText();

        if (bookID.isEmpty() || bookAuthor.isEmpty() || bookName.isEmpty() || bookPublisher.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in all fields.");
            return;
        }

        if (DataHelper.isBookExists(bookID)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Duplicate book id", "Book with same Book ID exists.\nPlease use new ID");
            return;
        }

        if (isInEditMode) {
            handleEditOperation();
            return;
        }

        Book book = new Book(bookID, bookName, bookAuthor, bookPublisher, Boolean.TRUE);
        boolean result = DataHelper.insertNewBook(book);
        if (result) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "New book added", bookName + " has been added");
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed to add new book", "Check all the entries and try again");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void checkData() {
        String qu = "SELECT title FROM BOOK";
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String titlex = rs.getString("title");
                System.out.println(titlex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void inflateUI(BookListController.Book book) {
        title.setText(book.getTitle());
        id.setText(book.getId());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        id.setEditable(false);
        isInEditMode = Boolean.TRUE;
    }

    private void clearEntries() {
        title.clear();
        id.clear();
        author.clear();
        publisher.clear();
    }

    private void handleEditOperation() {
        BookListController.Book book = new BookListController.Book(title.getText(), id.getText(), author.getText(), publisher.getText(), true);
        if (databaseHandler.updateBook(book)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success", "Update complete");
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed", "Could not update data");
        }
    }
}
