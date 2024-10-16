public interface UserDAO {
    void createUser(User user);
    User getUser(int id);
    void updateUser(User user);
    void deleteUser(int id);
    void createTableIfNotExists();
}