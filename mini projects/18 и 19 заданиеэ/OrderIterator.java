import java.util.List;
// Паттерн Iterator
public class OrderIterator implements Iterator<Order> {
    private List<Order> orders;
    private int currentIndex = 0;

    public OrderIterator(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < orders.size();
    }

    @Override
    public Order next() {
        return orders.get(currentIndex++);
    }
}
