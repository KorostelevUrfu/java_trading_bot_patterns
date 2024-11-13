//Паттерн Command
public class UpdateCustomerCommand implements Command {
    private DatabaseManager databaseManager;
    private int id;
    private String newName;

    public UpdateCustomerCommand(DatabaseManager databaseManager, int id, String newName) {
        this.databaseManager = databaseManager;
        this.id = id;
        this.newName = newName;
    }

    @Override
    public void execute() {
        try {
            databaseManager.updateCustomer(id, newName);
            System.out.println("Клиент обновлен: ID " + id + ", Новое имя: " + newName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
