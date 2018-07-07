package library.assistant.data.model;

/**
 * @author afsal
 */
public class MailServerInfo {

    private String mailServer;
    private Integer port;
    private String emailID;
    private String password;
    private Boolean sslEnabled;
    
    public MailServerInfo(String mailServer, Integer port, String emailID, String password, Boolean sslEnabled) {
        this.mailServer = mailServer;
        this.port = port;
        this.emailID = emailID;
        this.password = password;
        this.sslEnabled = sslEnabled;
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

    public Boolean getSslEnabled() {
        return sslEnabled;
    }
    
    @Override
    public String toString() {
        return String.format("%s:%d @ %s", mailServer, port, emailID);
    }

    public boolean validate() {
        boolean flag = mailServer == null || mailServer.isEmpty() || port == null || emailID == null || emailID.isEmpty() || password.isEmpty();
        return !flag;
    }
}
