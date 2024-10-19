package tinkoff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.google.protobuf.Timestamp;

import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

public class DataBase {

    private final String db_url = String.format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s&ssl=%s",
        DataStorage.db_host, DataStorage.db_port, DataStorage.db_name, DataStorage.db_user, DataStorage.db_password, DataStorage.db_ssl);

    private String figi_stream;
    private double close_price;
    private Timestamp time;
    private long volume;
    private List<String> figi_list = DataStorage.figiList;

    public DataBase(String figi_stream, double close_price, Timestamp time, long volume){
        this.figi_stream = figi_stream;
        this.close_price = close_price;
        this.time = time;
        this.volume = volume;
    }

    public DataBase(){

    }

    public void printCandleData(){
        System.out.println(
            "Данные свечи: "+ figi_stream + "\n" + 
            "close_price= " + close_price + "\n" +
            "time= " + time + 
            "volume= " + volume + "\n"
        );
    }

    //преобразует com.google.protobuf.Timestamp в java.sql.Timestamp иначе будет ошибка вставки данных времени
    private java.sql.Timestamp rebuildTimestamp(){
        long seconds = time.getSeconds();
        int nanos = time.getNanos();
        java.sql.Timestamp sql_time = new java.sql.Timestamp(seconds * 1000 + nanos / 1000000);
        return sql_time;
    }

    public void createFigiTable(){
        try (Connection conn = DriverManager.getConnection(db_url); Statement state = conn.createStatement()) {
            for(String figi : figi_list){
                String create_table = "CREATE TABLE IF NOT EXISTS " + figi + " (close_price REAL, time TIMESTAMP);";
                state.execute(create_table);
                System.out.printf("Успешно создана таблица %s.\n", figi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertFigiData(){
        String insert_data = "INSERT INTO " + figi_stream + " (close_price, time) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(db_url); PreparedStatement preparedState = conn.prepareStatement(insert_data)) {
            preparedState.setDouble(1, close_price);
            preparedState.setTimestamp(2, rebuildTimestamp());
            int rowsInserted = preparedState.executeUpdate();
            if (rowsInserted > 0) {
                System.out.printf("Данные свечи успешно добавлены в таблицу %s.\n", figi_stream);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectData(){
        try (Connection conn = DriverManager.getConnection(db_url);
        Statement state = conn.createStatement();) {
            for(String figi:figi_list){
                ResultSet select_data = state.executeQuery("SELECT close_price FROM " + figi + " ORDER BY time;");
                while (select_data.next()) {
                    double closePrice = select_data.getDouble("close_price");
                    System.out.println("Close Price: " + closePrice + " " + figi); //позже будет передавать в класс стратегий
                }
            } 
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void deleteData(){
        try (Connection conn = DriverManager.getConnection(db_url); Statement state = conn.createStatement();) {
            for (String figi : figi_list){
                String delete_data = String.format("DELETE FROM %s;", figi);
                state.execute(delete_data);
                System.out.println("Данные удалены из таблицы " + figi);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public void insertHistoricData(){
    //     for(String figi: figi_list){
    //         List<HistoricCandle> candles1min = CreateToken.getToken().getMarketDataService()
    //         .getCandlesSync(figi, Instant.now().minus(1, ChronoUnit.DAYS), Instant.now(),
    //         CandleInterval.CANDLE_INTERVAL_1_MIN);

    //         for(int i = 0; i < candles1min.size(); i++){
    //         double close_price = candles1min.get(i).getClose().getUnits() + candles1min.get(i).getClose().getNano() * Math.pow(10, -9);
    //         com.google.protobuf.Timestamp time = candles1min.get(i).getTime();

    //             String insert = "INSERT INTO " + figi + " (close_price, time) VALUES (?, ?)";

    //             try (Connection conn = DriverManager.getConnection(db_url);
    //             PreparedStatement preparedStatement = conn.prepareStatement(insert)) {
    
    //             preparedStatement.setDouble(1, close_price);
    //             preparedStatement.setTimestamp(2, new Timestamp());
    
    //             int rowsInserted = preparedStatement.executeUpdate();
    //             if (rowsInserted > 0) {
    //                 System.out.printf("Данные свечи успешно добавлены в таблицу %s.\n", figi);
    //             }
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //         }
    //     }   
    // }

}








