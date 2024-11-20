import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class Database {
    private static final String url = "jdbc:postgresql://localhost:5432/Library";
    private static final String user = "postgres";
    private static final String password = "1";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void createTablesIfNotExist() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
             
            String createBooksTable = "CREATE TABLE IF NOT EXISTS books " +
                    "(id SERIAL PRIMARY KEY, title VARCHAR(100) NOT NULL, author VARCHAR(100) NOT NULL)";
            statement.executeUpdate(createBooksTable);

            String createUsersTable = "CREATE TABLE IF NOT EXISTS users " +
                    "(id SERIAL PRIMARY KEY, username VARCHAR(50) NOT NULL, role VARCHAR(10) NOT NULL)";
            statement.executeUpdate(createUsersTable);

            String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders " +
                    "(id SERIAL PRIMARY KEY, user_id INT REFERENCES users(id), book_id INT REFERENCES books(id), status VARCHAR(20) NOT NULL DEFAULT 'Pending')";
            statement.executeUpdate(createOrdersTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int addUser(String username, String role) {
        String insertUserSQL = "INSERT INTO users (username, role) VALUES (?, ?) RETURNING id";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertUserSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, role);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); //возвращаем id
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void addBook(String title, String author) {
        String insertBookSQL = "INSERT INTO books (title, author) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertBookSQL)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static int executeAndReturnId(PreparedStatement pstmt) throws SQLException {
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1); //возвращаем id
        }
        return -1;
    }
}
