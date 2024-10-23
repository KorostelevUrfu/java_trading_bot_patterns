public class Main {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager("jdbc:postgresql://localhost:5432/Users", "postgres", "1");
        
        //добавление ролей и пользователей
        Role adminRole = new Role("Administrator");
        User user1 = new User("JohnDoe", adminRole);
        
        adminRole.addUser(user1);
        

        //создание роли администратора
        dbManager.addUser(user1.getUsername(), 1); 
        
        //печать пользователей
        System.out.println("Users in Database:");
        dbManager.printUsers();
        
        //декорирование пользователя
        UserComponent decoratedUser = new StatusDecorator(user1, "Active");
        decoratedUser.displayInfo();
    }
}
