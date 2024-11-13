public class Main {
    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.createTableIfNotExists();
        RemoteControl remote = new RemoteControl();

        // Добавление клиента
        Command addCommand = new AddCustomerCommand(databaseManager, "Иван Иванов");
        remote.setCommand(addCommand);
        remote.pressButton();

        // Обновление клиента
        Command updateCommand = new UpdateCustomerCommand(databaseManager, 1, "Иван Петров");
        remote.setCommand(updateCommand);
        remote.pressButton();

        // Удаление клиента
        Command deleteCommand = new DeleteCustomerCommand(databaseManager, 1);
        remote.setCommand(deleteCommand);
        remote.pressButton();
    }
}
