package library.assistant.database;

import java.sql.Connection;

public class DatabaseHandler {
    private static DatabaseHandler handler = null;
    private Connection conn = null;

    private DatabaseHandler() {}

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    public Connection getConnection() {
        return conn;
    }

    // Helper method for testing
    public void setConnection(Connection conn) {
        this.conn = conn;
    }
}
