import java.util.HashMap;
import java.util.Map;

//Паттерн Facade
public class TransportFactory {
    private Map<String, Transport> transports = new HashMap<>();

    public Transport getTransport(String type, String model) {
        String key = type + model;
        if (!transports.containsKey(key)) {
            transports.put(key, new Transport(type, model));
            System.out.println("Creating new transport: " + model);
        }
        return transports.get(key);
    }
}
