public class StandardUserService extends UserService {

    public StandardUserService(UserDAO userDAO) {
        super(userDAO);
    }

    @Override
    public void registerUser(User user) {
        userDAO.createUser(user);
    }

    @Override
    public User fetchUser(int id) {
        return userDAO.getUser(id);
    }
}
