public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new PostgresUserDAO();

        userDAO.createTableIfNotExists();

        UserService userService = new StandardUserService(userDAO);

        //создание пользователя
        User newUser = new User("Иван Иванов", "ivan@gmail.com");
        userService.registerUser(newUser);

        //получение пользователя
        User fetchedUser = userService.fetchUser(1);
        System.out.println("Полученный пользователь: " + fetchedUser);

        //обновление пользователя
        fetchedUser.setEmail("ivanov@gmail.com");
        userDAO.updateUser(fetchedUser);

        //удаление пользователя
        userDAO.deleteUser(1);
    }
}
