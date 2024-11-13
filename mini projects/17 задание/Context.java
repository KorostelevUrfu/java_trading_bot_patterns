import java.util.HashMap;
import java.util.Map;

//Паттерн Interpreter
public class Context {
    private Map<String, Integer> variables = new HashMap<>();

    public void assign(String variable, int value) {
        variables.put(variable, value);
    }

    public int getValue(String variable) {
        return variables.get(variable);
    }
}
