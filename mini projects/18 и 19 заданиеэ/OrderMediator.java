import java.util.ArrayList;
import java.util.List;
// Паттерн Mediator
public class OrderMediator {
    private List<Order> orders = new ArrayList<>();
    private List<Book> availableBooks = new ArrayList<>();

    public void addBook(Book book) {
        availableBooks.add(book);
    }

    public void placeOrder(User user, Book book) {
        if (user.getRole().equals("customer")) {
            Order order = new Order(orders.size() + 1, user, book, "Pending");
            orders.add(order);
            System.out.println(user.getUsername() + " сделал заказ на: " + book.getTitle());
        } else {
            System.out.println("Только клиенты могут делать заказы.");
        }
    }

    public OrderIterator getOrderIterator() {
        return new OrderIterator(orders);
    }

    public void approveOrder(int orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                order.setStatus("Approved");
                System.out.println("Заказ " + orderId + " был подтвержден.");
                return;
            }
        }
        System.out.println("Заказ не найден.");
    }
}
