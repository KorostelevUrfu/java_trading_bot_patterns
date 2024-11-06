public class Main {
    public static void main(String[] args) {
        //создание цепочки обработчиков
        ValidationHandler formatValidator = new FormatValidationHandler();
        ValidationHandler lengthValidator = new LengthValidationHandler();
        ValidationHandler specialCharacterValidator = new SpecialCharacterValidationHandler();

        //настройка цепочки
        formatValidator.setNextHandler(lengthValidator);
        lengthValidator.setNextHandler(specialCharacterValidator);

        //примеры ввода
        String[] testInputs = {"abc", "12a", "a!", "ab", "abc123", "abcd"};

        for (String input : testInputs) {
            System.out.println("Проверка ввода: " + input);
            formatValidator.validate(input);
            System.out.println();
        }
        // Пример вывода
        // Проверка ввода: abc

        // Проверка ввода: 12a
        // Ошибка: Ввод должен содержать только буквы.

        // Проверка ввода: a!
        // Ошибка: Ввод должен содержать только буквы.

        // Проверка ввода: ab
        // Ошибка: Ввод должен содержать минимум 3 символа.

        // Проверка ввода: abc123
        // Ошибка: Ввод должен содержать только буквы.

        // Проверка ввода: abcd
    }
}
