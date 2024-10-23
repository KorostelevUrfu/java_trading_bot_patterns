import java.sql.*;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            initDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initDatabase() {
        createRolesTable();
        createUsersTable();
    }

    private void createRolesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS roles (" +
                     "id INT PRIMARY KEY AUTO_INCREMENT, " +
                     "name VARCHAR(50) NOT NULL)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Таблица roles успешно создана/существует");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     "id INT PRIMARY KEY AUTO_INCREMENT, " +
                     "username VARCHAR(50) NOT NULL, " +
                     "role_id INT, " +
                     "FOREIGN KEY (role_id) REFERENCES roles(id))";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Таблица users успешно создана/существует");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String username, int roleId) {
        String sql = "INSERT INTO users (username, role_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, roleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printUsers() {
        String sql = "SELECT u.username, r.name FROM users u " +
                     "JOIN roles r ON u.role_id = r.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("User: " + rs.getString(1) + ", Role: " + rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
