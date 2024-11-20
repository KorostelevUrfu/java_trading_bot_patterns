public class Order {
    private int id;
    private User user;
    private Book book;
    private String status;

    public Order(int id, User user, Book book, String status) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
