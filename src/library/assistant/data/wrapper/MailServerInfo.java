package library.assistant.data.wrapper;

/**
 * @author afsal
 */
public class MailServerInfo {

    private String mailServer;
    private Integer port;
    private String emailID;
    private String password;

    public MailServerInfo(String mailServer, Integer port, String emailID, String password) {
        this.mailServer = mailServer;
        this.port = port;
        this.emailID = emailID;
        this.password = password;
    }

    public String getMailServer() {
        return mailServer;
    }

    public Integer getPort() {
        return port;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("%s:%d @ %s", mailServer, port, emailID);
    }

    public boolean validate() {
        boolean flag = mailServer==null || mailServer.isEmpty() || port == null || emailID == null || emailID.isEmpty() || password.isEmpty();
        return !flag;
    }
}
