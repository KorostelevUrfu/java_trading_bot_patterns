package tinkoff;

import java.util.List;

import java.io.File;

//хранение переменных
public class DataStorage {
        

    public static final String token = "";
    
    public static List<String> figiList = List.of(
        "BBG004730ZJ9"
    );

    public static final String db_host = "localhost";
    public static final String db_port = "5432";
    public static final String db_name = "JavaTradingBotTinkoff";
    public static final String db_user= "postgres";
    public static final String db_password = "1";
    public static final String db_ssl = "False";

    //путь к файлу с архивами котировок
    public static final File path = new File("C:\\Users\\user\\Desktop\\Tinkoff data\\Stock_Quotes");
    
}