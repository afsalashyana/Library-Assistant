package library.assistant.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import library.assistant.data.wrapper.Book;
import library.assistant.ui.listmember.MemberListController.Member;

/**
 *
 * @author afsal
 */
public class DataHelper {

    public static boolean insertNewBook(Book book) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
                    "INSERT INTO BOOK(id,title,author,publisher,isAvail) VALUES(?,?,?,?,?)");
            statement.setString(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPublisher());
            statement.setBoolean(5, book.getAvailability());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DataHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean insertNewMember(Member member) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
                    "INSERT INTO BOOK(id,name,mobile,email) VALUES(?,?,?,?)");
            statement.setString(1, member.getId());
            statement.setString(2, member.getName());
            statement.setString(3, member.getMobile());
            statement.setString(4, member.getEmail());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DataHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean isBookExists(String id) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM BOOK WHERE id=?";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println(count);
                return (count > 0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean isMemberExists(String id) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM MEMBER WHERE id=?";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println(count);
                return (count > 0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
