import java.sql.*;
//Паттерн Composite
public class User implements UserComponent {
    private String username;
    private Role role;

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    @Override
    public void displayInfo() {
        System.out.println("User: " + username + ", Role: " + role.getName());
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }
}
