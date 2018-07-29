package library.assistant.ui.notifoverdue;

import com.google.common.collect.ImmutableList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import library.assistant.alert.AlertMaker;
import library.assistant.data.model.MailServerInfo;
import library.assistant.database.DataHelper;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.notifoverdue.emailsender.EmailSenderController;
import library.assistant.ui.settings.Preferences;
import library.assistant.util.LibraryAssistantUtil;

/**
 * FXML Controller class
 *
 * @author Villan
 */
public class OverdueNotificationController implements Initializable {

    private ObservableList<NotificationItem> list = FXCollections.observableArrayList();
    @FXML
    private StackPane rootPane;
    @FXML
    private TableView<NotificationItem> tableview;
    @FXML
    private TableColumn<NotificationItem, JFXCheckBox> colNotify;
    @FXML
    private TableColumn<NotificationItem, String> colMemID;
    @FXML
    private TableColumn<NotificationItem, String> colMemberName;
    @FXML
    private TableColumn<NotificationItem, String> colEmail;
    @FXML
    private TableColumn<NotificationItem, String> colBookName;
    @FXML
    private TableColumn<NotificationItem, Integer> colDays;
    @FXML
    private TableColumn<NotificationItem, Float> colFineAmount;
    @FXML
    private AnchorPane contentPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        checkForMailServerConfig();
        initialize();
        loadData();
    }

    private void initialize() {
        colNotify.setCellValueFactory(new NotificationControlCellValueFactory());
        colMemID.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("memberEmail"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colDays.setCellValueFactory(new PropertyValueFactory<>("dayCount"));
        colFineAmount.setCellValueFactory(new PropertyValueFactory<>("fineAmount"));
        tableview.setItems(list);
    }

    private void loadData() {
        list.clear();

        Preferences pref = Preferences.getPreferences();
        Long overdueBegin = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(pref.getnDaysWithoutFine());

        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT ISSUE.bookID, ISSUE.memberID, ISSUE.issueTime, MEMBER.name, MEMBER.id, MEMBER.email, BOOK.title FROM ISSUE\n"
                + "LEFT OUTER JOIN MEMBER\n"
                + "ON MEMBER.id = ISSUE.memberID\n"
                + "LEFT OUTER JOIN BOOK\n"
                + "ON BOOK.id = ISSUE.bookID\n"
                + "WHERE ISSUE.issueTime < ?";
        try {
            PreparedStatement statement = handler.getConnection().prepareStatement(qu);
            statement.setTimestamp(1, new Timestamp(overdueBegin));
            ResultSet rs = statement.executeQuery();
            int counter = 0;
            while (rs.next()) {
                counter += 1;
                String memberName = rs.getString("name");
                String memberID = rs.getString("id");
                String email = rs.getString("email");
                String bookID = rs.getString("bookID");
                String bookTitle = rs.getString("title");
                Timestamp issueTime = rs.getTimestamp("issueTime");
                System.out.println("Issued on " + issueTime);
                Integer days = Math.toIntExact(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - issueTime.getTime())) + 1;
                Float fine = LibraryAssistantUtil.getFineAmount(days);

                NotificationItem item = new NotificationItem(true, memberID, memberName, email, bookTitle, LibraryAssistantUtil.getDateString(issueTime), days, fine);
                list.add(item);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleSendNotificationAction(ActionEvent event) {
        List<NotificationItem> selectedItems = list.stream().filter(item -> item.getNotify()).collect(Collectors.toList());
        if (selectedItems.isEmpty()) {
            AlertMaker.showErrorMessage("Nothing Selected", "Nothing selected to notify");
            return;
        }
        Object controller = LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/notifoverdue/emailsender/email_sender.fxml"), "Notify Overdue", null);
        if (controller != null) {
            EmailSenderController cont = (EmailSenderController) controller;
            cont.setNotifRequestData(selectedItems);
            cont.start();
        }
    }

    private void checkForMailServerConfig() {
        JFXButton button = new JFXButton("Okay");
        button.setOnAction((ActionEvent event) -> {
            ((Stage) rootPane.getScene().getWindow()).close();
        });
        MailServerInfo mailServerInfo = DataHelper.loadMailServerInfo();
        System.out.println(mailServerInfo);
        if (mailServerInfo == null || !mailServerInfo.validate()) {
            System.out.println("Mail server not configured");
            AlertMaker.showMaterialDialog(rootPane, contentPane, ImmutableList.of(button), "Mail server is not configured", "Please configure mail server first.\nIt is available under Settings");
        }
    }

    public static class NotificationControlCellValueFactory implements Callback<CellDataFeatures<NotificationItem, JFXCheckBox>, ObservableValue<JFXCheckBox>> {

        @Override
        public ObservableValue<JFXCheckBox> call(TableColumn.CellDataFeatures<NotificationItem, JFXCheckBox> param) {
            NotificationItem item = param.getValue();
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.selectedProperty().setValue(item.getNotify());
            checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
                item.setNotify(new_val);
            });
            return new SimpleObjectProperty<>(checkBox);
        }
    }
}
