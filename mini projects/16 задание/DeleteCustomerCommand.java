//Паттерн Command
public class DeleteCustomerCommand implements Command {
    private DatabaseManager databaseManager;
    private int id;

    public DeleteCustomerCommand(DatabaseManager databaseManager, int id) {
        this.databaseManager = databaseManager;
        this.id = id;
    }

    @Override
    public void execute() {
        try {
            String name = databaseManager.getCustomerName(id);
            databaseManager.deleteCustomer(id);
            System.out.println("Клиент удален: ID " + id + ", Имя: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
