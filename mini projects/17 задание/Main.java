public class Main {
    public static void main(String[] args) {
        //cоздаем контекст
        Context context = new Context();
        
        //присваиваем значения переменным
        context.assign("x", 5);
        context.assign("y", 10);

        //создаем выражения
        Expression expression1 = new Addition(new Number(2), new Number(3)); //2 + 3
        Expression expression2 = new Subtraction(new Number(5), new Addition(new Number(2), new Number(3))); //5 - (2 + 3)

        //интерпретируем и выводим результаты
        System.out.println("Результат выражения 2 + 3: " + expression1.interpret(context)); //вывод - 5
        System.out.println("Результат выражения 5 - (2 + 3): " + expression2.interpret(context)); //вывод - 0
    }
}
