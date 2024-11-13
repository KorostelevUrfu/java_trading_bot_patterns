//Паттерн Command
public class AddCustomerCommand implements Command {
    private DatabaseManager databaseManager;
    private String name;

    public AddCustomerCommand(DatabaseManager databaseManager, String name) {
        this.databaseManager = databaseManager;
        this.name = name;
    }

    @Override
    public void execute() {
        try {
            databaseManager.addCustomer(name);
            System.out.println("Клиент добавлен: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
