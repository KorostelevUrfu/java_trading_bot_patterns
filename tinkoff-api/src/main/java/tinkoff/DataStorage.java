package tinkoff;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//хранение переменных
public class DataStorage {
        

    public static final String token = "";

    public static String[] tickers = {"SBER", "GAZP", "LKOH", "YDEX", "ROSN", "TATN", "SIBN"}; //голубые фишки (не всЁ и не все)

    public static Map<String, Integer> interval = new HashMap<>() {{
        //значения переведены в миллисекунды для Timestamp
        put("1 min", 60000);
        put("2 min", 120000);
        put("3 min", 180000);
        put("5 min", 300000);
        put("10 min", 600000);
        put("30 min", 1800000);
        put("1 hour", 3600000);
        put("2 hours", 7200000);
        put("4 hours", 14400000);
        put("1 day", 86400000);
        put("1 week", 604800000);
        put("1 month", 1);
    }};

    // "1 min", 1,
    //     "2 min", 2,
    //     "3 min", 3,
    //     "5 min", 4,
    //     "10 min", 5,
    //     "30 min", 6,
    //     "1 hour", 7,
    //     "2 hours", 8,
    //     "4 hours", 9,
    //     "1 day", 10,
    //     "1 week", 11,
    //     "1 month", 12
    public static final String db_host = "localhost";
    public static final String db_port = "5432";
    public static final String db_name = "JavaTradingBotTinkoff";
    public static final String db_user= "postgres";
    public static final String db_password = "1";
    public static final String db_ssl = "False";

    //путь к файлу с архивами котировок
    public static final File path = new File("C:\\Users\\user\\Desktop\\Tinkoff data\\Stock_Quotes");
    
}