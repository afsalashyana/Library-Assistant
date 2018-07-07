package library.assistant.ui.main.toolbar;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import library.assistant.ui.callback.BookReturnCallback;
import library.assistant.ui.issuedlist.IssuedListController;
import library.assistant.util.LibraryAssistantUtil;

public class ToolbarController implements Initializable {

    private BookReturnCallback callback;

    public void setBookReturnCallback(BookReturnCallback callback) {
        this.callback = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void loadAddMember(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/addmember/member_add.fxml"), "Add New Member", null);
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/addbook/add_book.fxml"), "Add New Book", null);
    }

    @FXML
    private void loadMemberTable(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/listmember/member_list.fxml"), "Member List", null);
    }

    @FXML
    private void loadBookTable(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/listbook/book_list.fxml"), "Book List", null);
    }

    @FXML
    private void loadSettings(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/settings/settings.fxml"), "Settings", null);
    }

    @FXML
    private void loadIssuedBookList(ActionEvent event) {
        Object controller = LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/issuedlist/issued_list.fxml"), "Issued Book List", null);
        if (controller != null) {
            IssuedListController cont = (IssuedListController) controller;
            cont.setBookReturnCallback(callback);
        }
    }

}
