import java.util.ArrayList;
import java.util.List;
//Паттерн Composite
public class Role implements UserComponent {
    private String name;
    private List<UserComponent> users = new ArrayList<>();

    public Role(String name) {
        this.name = name;
    }

    public void addUser(UserComponent user) {
        users.add(user);
    }

    public String getName() {
        return name;
    }

    @Override
    public void displayInfo() {
        System.out.println("Role: " + name);
        for (UserComponent user : users) {
            user.displayInfo();
        }
    }
}
