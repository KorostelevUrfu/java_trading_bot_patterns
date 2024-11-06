// Паттерн Chain of responsibility

//только буквы
class FormatValidationHandler extends ValidationHandler {
    @Override
    public void validate(String input) {
        if (!input.matches("[a-zA-Z]+")) {
            System.out.println("Ошибка: Ввод должен содержать только буквы.");
        } else if (nextHandler != null) {
            nextHandler.validate(input);
        }
    }
}

//минимум 3 символа
class LengthValidationHandler extends ValidationHandler {
    @Override
    public void validate(String input) {
        if (input.length() < 3) {
            System.out.println("Ошибка: Ввод должен содержать минимум 3 символа.");
        } else if (nextHandler != null) {
            nextHandler.validate(input);
        }
    }
}

//для проверки наличия специальных символов
class SpecialCharacterValidationHandler extends ValidationHandler {
    @Override
    public void validate(String input) {
        if (input.matches(".*[^a-zA-Z].*")) {
            System.out.println("Ошибка: Ввод не должен содержать специальные символы.");
        } else if (nextHandler != null) {
            nextHandler.validate(input);
        }
    }
}
