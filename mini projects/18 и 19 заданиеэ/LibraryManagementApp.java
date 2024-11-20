import java.sql.*;

public class LibraryManagementApp {
    public static void main(String[] args) {
        Database.createTablesIfNotExist();

        //создание медиатора
        OrderMediator mediator = new OrderMediator();

        //добавление книг в медиатор
        Database.addBook("Война и мир", "Лев Толстой");
        Database.addBook("Анна Каренина", "Лев Толстой");
        Database.addBook("Преступление и наказание", "Фёдор Достоевский");
        Database.addBook("Идиот", "Фёдор Достоевский");
        Database.addBook("Мастер и Маргарита", "Михаил Булгаков");

        int customerId = Database.addUser("Александр", "customer");
        int librarianId = Database.addUser("Сергей", "librarian");

        //создание пользователей для медиатора
        User customer = new User(customerId, "Александр", "customer");
        User librarian = new User(librarianId, "Сергей", "librarian");

        //клиент делает заказы
        mediator.placeOrder(customer, new Book(1, "Война и мир", "Лев Толстой")); 
        mediator.placeOrder(customer, new Book(2, "Анна Каренина", "Лев Толстой"));

        //итерация по заказам
        System.out.println("\nСписок заказов:");
        OrderIterator orderIterator = mediator.getOrderIterator();
        while (orderIterator.hasNext()) {
            Order order = orderIterator.next();
            System.out.println("ID заказа: " + order.getId() + ", Книга: " + order.getBook().getTitle() + ", Статус: " + order.getStatus());
        }

        //подтверждение заказа
        mediator.approveOrder(1);
    }
}
