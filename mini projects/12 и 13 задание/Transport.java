//Паттерн Facade
public class Transport {
    private String type;
    private String model;
    
    public Transport(String type, String model) {
        this.type = type;
        this.model = model;
    }
    
    public String getType() {
        return type;
    }
    
    public String getModel() {
        return model;
    }
    
    public void display() {
        System.out.println("Transport Type: " + type + ", Model: " + model);
    }
}
