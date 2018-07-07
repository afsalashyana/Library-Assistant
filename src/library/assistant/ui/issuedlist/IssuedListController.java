package library.assistant.ui.issuedlist;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.settings.Preferences;
import library.assistant.ui.callback.BookReturnCallback;
import library.assistant.util.LibraryAssistantUtil;

/*
 * @author afsal villan
 */
public class IssuedListController implements Initializable {

    private ObservableList<IssueInfo> list = FXCollections.observableArrayList();
    private BookReturnCallback callback;

    @FXML
    private TableView<IssueInfo> tableView;
    @FXML
    private TableColumn<IssueInfo, Integer> idCol;
    @FXML
    private TableColumn<IssueInfo, String> bookIDCol;
    @FXML
    private TableColumn<IssueInfo, String> bookNameCol;
    @FXML
    private TableColumn<IssueInfo, String> holderNameCol;
    @FXML
    private TableColumn<IssueInfo, String> issueCol;
    @FXML
    private TableColumn<IssueInfo, Integer> daysCol;
    @FXML
    private TableColumn<IssueInfo, Float> fineCol;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane contentPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookIDCol.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        holderNameCol.setCellValueFactory(new PropertyValueFactory<>("holderName"));
        issueCol.setCellValueFactory(new PropertyValueFactory<>("dateOfIssue"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("days"));
        fineCol.setCellValueFactory(new PropertyValueFactory<>("fine"));
        tableView.setItems(list);
    }

    public void setBookReturnCallback(BookReturnCallback callback) {
        this.callback = callback;
    }

    private void loadData() {
        list.clear();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT ISSUE.bookID, ISSUE.memberID, ISSUE.issueTime, MEMBER.name, BOOK.title FROM ISSUE\n"
                + "LEFT OUTER JOIN MEMBER\n"
                + "ON MEMBER.id = ISSUE.memberID\n"
                + "LEFT OUTER JOIN BOOK\n"
                + "ON BOOK.id = ISSUE.bookID";
        ResultSet rs = handler.execQuery(qu);
        Preferences pref = Preferences.getPreferences();
        try {
            int counter = 0;
            while (rs.next()) {
                counter += 1;
                String memberName = rs.getString("name");
                String bookID = rs.getString("bookID");
                String bookTitle = rs.getString("title");
                Timestamp issueTime = rs.getTimestamp("issueTime");
                System.out.println("Issued on " + issueTime);
                Integer days = Math.toIntExact(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - issueTime.getTime())) + 1;
                Float fine = LibraryAssistantUtil.getFineAmount(days);
                IssueInfo issueInfo = new IssueInfo(counter, bookID, bookTitle, memberName, LibraryAssistantUtil.formatDateTimeString(new Date(issueTime.getTime())), days, fine);
                list.add(issueInfo);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadData();
    }

    @FXML
    private void exportAsPDF(ActionEvent event) {
        List<List> printData = new ArrayList<>();
        String[] headers = {"SI", "BOOK ID", "      BOOK NAME       ", "    HOLDER NAME     ", "ISSUE DATE", "DAYS ELAPSED", "FINE"};
        printData.add(Arrays.asList(headers));
        for (IssueInfo info : list) {
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(info.getId()));
            row.add(info.getBookID());
            row.add(info.getBookName());
            row.add(info.getHolderName());
            row.add(info.getDateOfIssue());
            row.add(String.valueOf(info.getDays()));
            row.add(String.valueOf(info.getFine()));
            printData.add(row);
        }
        LibraryAssistantUtil.initPDFExprot(rootPane, contentPane, getStage(), printData);
    }

    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    @FXML
    private void handleReturn(ActionEvent event) {
        IssueInfo issueInfo = tableView.getSelectionModel().getSelectedItem();
        if (issueInfo != null) {
            callback.loadBookReturn(issueInfo.getBookID());
        }
    }

    public static class IssueInfo {

        private final SimpleIntegerProperty id;
        private final SimpleStringProperty bookID;
        private final SimpleStringProperty bookName;
        private final SimpleStringProperty holderName;
        private final SimpleStringProperty dateOfIssue;
        private final SimpleIntegerProperty nDays;
        private final SimpleFloatProperty fine;

        public IssueInfo(int id, String bookID, String bookName, String holderName, String dateOfIssue, Integer nDays, float fine) {
            this.id = new SimpleIntegerProperty(id);
            this.bookID = new SimpleStringProperty(bookID);
            this.bookName = new SimpleStringProperty(bookName);
            this.holderName = new SimpleStringProperty(holderName);
            this.dateOfIssue = new SimpleStringProperty(dateOfIssue);
            this.nDays = new SimpleIntegerProperty(nDays);
            this.fine = new SimpleFloatProperty(fine);
            System.out.println(this.nDays.get());
        }

        public Integer getId() {
            return id.get();
        }

        public String getBookID() {
            return bookID.get();
        }

        public String getBookName() {
            return bookName.get();
        }

        public String getHolderName() {
            return holderName.get();
        }

        public String getDateOfIssue() {
            return dateOfIssue.get();
        }

        public Integer getDays() {
            return nDays.get();
        }

        public Float getFine() {
            return fine.get();
        }
    }
}
