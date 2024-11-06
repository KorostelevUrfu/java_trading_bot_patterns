// Паттерн Chain of responsibility
abstract class ValidationHandler {
    protected ValidationHandler nextHandler;

    public void setNextHandler(ValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void validate(String input);
}
