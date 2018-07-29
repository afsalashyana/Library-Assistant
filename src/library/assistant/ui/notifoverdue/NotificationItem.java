package library.assistant.ui.notifoverdue;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class NotificationItem {

    private final SimpleBooleanProperty notify;
    private final SimpleStringProperty memberID;
    private final SimpleStringProperty memberName;
    private final SimpleStringProperty memberEmail;
    private final SimpleStringProperty bookName;
    private final SimpleIntegerProperty dayCount;
    private final SimpleFloatProperty fineAmount;
    private final SimpleStringProperty issueDate;

    public NotificationItem(boolean notify, String memberID, String memberName, String memberEmail, String bookName, String issueDate, int dayCount, float fineAmount) {
        this.notify = new SimpleBooleanProperty(notify);
        this.memberID = new SimpleStringProperty(memberID);
        this.memberName = new SimpleStringProperty(memberName);
        this.memberEmail = new SimpleStringProperty(memberEmail);
        this.bookName = new SimpleStringProperty(bookName);
        this.dayCount = new SimpleIntegerProperty(dayCount);
        this.fineAmount = new SimpleFloatProperty(fineAmount);
        this.issueDate = new SimpleStringProperty(issueDate);
    }

    public Boolean getNotify() {
        return notify.get();
    }

    public String getMemberID() {
        return memberID.get();
    }

    public String getMemberName() {
        return memberName.get();
    }

    public String getMemberEmail() {
        return memberEmail.get();
    }

    public String getBookName() {
        return bookName.get();
    }

    public Integer getDayCount() {
        return dayCount.get();
    }

    public String getFineAmount() {
        return String.format("$ %.2f", fineAmount.get());
    }

    public void setNotify(Boolean val) {
        notify.set(val);
    }

    public String getIssueDate() {
        return issueDate.get();
    }
}
