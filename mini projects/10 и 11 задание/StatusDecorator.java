//Паттерн Decorator
public class StatusDecorator extends UserDecorator {
    private String status;

    public StatusDecorator(UserComponent user, String status) {
        super(user);
        this.status = status;
    }

    @Override
    public void displayInfo() {
        user.displayInfo();
        System.out.println("Status: " + status);
    }
}