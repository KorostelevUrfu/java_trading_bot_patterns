//Паттерн Flyweight
public class TransportSystem {
    public static void main(String[] args) {

        TransportFacade transportFacade = new TransportFacade("jdbc:postgresql://localhost:5432/Transports", "postgres", "1");
        transportFacade.addTransport("Car", "Toyota Camry");
        transportFacade.addTransport("Car", "Honda Accord");
        transportFacade.addTransport("Bike", "Trek FX 3");
        transportFacade.addTransport("Car", "Toyota Camry"); //этот экземпляр не добавиться, так как уже есть такие атритбуты. Позволяет экономить память.

    }
}
