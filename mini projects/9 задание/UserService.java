//Паттерн Bridge
public abstract class UserService {
    protected UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public abstract void registerUser(User user);
    public abstract User fetchUser(int id);
}
