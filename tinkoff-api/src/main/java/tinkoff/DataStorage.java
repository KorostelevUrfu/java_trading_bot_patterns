package tinkoff;

import java.io.File;

//хранение переменных
public class DataStorage {
        

    public static final String token = "";

    public static String[] tickers = {"SBER", "GAZP", "LKOH", "YDEX", "ROSN", "TATN", "SIBN"}; //голубые фишки (не всЁ и не все)

    public static final String db_host = "localhost";
    public static final String db_port = "5432";
    public static final String db_name = "JavaTradingBotTinkoff";
    public static final String db_user= "postgres";
    public static final String db_password = "1";
    public static final String db_ssl = "False";

    //путь к файлу с архивами котировок
    public static final File path = new File("C:\\Users\\user\\Desktop\\Tinkoff data\\Stock_Quotes");
    
}