import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TransportFacade {
    private TransportFactory transportFactory = new TransportFactory();
    private Connection connection;

    public TransportFacade(String dbUrl, String user, String password) {
        try {
            connection = DriverManager.getConnection(dbUrl, user, password);
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS transport (" +
                     "id SERIAL PRIMARY KEY, " +
                     "type VARCHAR(100), " +
                     "model VARCHAR(100), " +
                     "unique (type, model));";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void addTransport(String type, String model) {
        Transport transport = transportFactory.getTransport(type, model);
        transport.display();
        insertTransports(transport);
    }
    
    private void insertTransports(Transport transport) {
        String sql = "INSERT INTO transport (type, model) VALUES ('" +
                     transport.getType() + "', '" +
                     transport.getModel() + "') " +
                     "ON CONFLICT (type, model) DO NOTHING;";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
