//Паттерн Decorator
public abstract class UserDecorator implements UserComponent {
    protected UserComponent user;

    public UserDecorator(UserComponent user) {
        this.user = user;
    }

    public abstract void displayInfo();
}

