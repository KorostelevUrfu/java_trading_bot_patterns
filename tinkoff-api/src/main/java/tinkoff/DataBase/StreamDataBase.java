package tinkoff.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.Timestamp;

import tinkoff.DataStorage;


public class StreamDataBase {

    protected final String db_url = String.format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s&ssl=%s",
        DataStorage.db_host, DataStorage.db_port, DataStorage.db_name, DataStorage.db_user, DataStorage.db_password, DataStorage.db_ssl);

    protected String figi;
    protected double close_price;
    protected Timestamp time;
    protected long volume;
    protected int candle_interval;

    public StreamDataBase(String figi_stream, double close_price, Timestamp time, long volume){
        this.figi = figi_stream;
        this.close_price = close_price;
        this.time = time;
        this.volume = volume;
    }

    public StreamDataBase(){

    }

    public void printCandleData(){
        System.out.println(
            "Данные свечи: "+ figi + "\n" + 
            "close_price= " + close_price + "\n" +
            "time= " + time + 
            "volume= " + volume + "\n"
        );
    }

    //преобразует com.google.protobuf.Timestamp в java.sql.Timestamp иначе будет ошибка вставки данных времени
    protected java.sql.Timestamp rebuildTimestamp(){
        long seconds = time.getSeconds();
        int nanos = time.getNanos();
        java.sql.Timestamp sql_time = new java.sql.Timestamp(seconds * 1000 + nanos / 1000000);
        return sql_time;
    }

    public void createFigiTable(){
        try (Connection conn = DriverManager.getConnection(db_url); Statement state = conn.createStatement()) {
            String create_table = "CREATE TABLE IF NOT EXISTS " + figi + " (close_price REAL, time TIMESTAMP, volume INT);";
            state.execute(create_table);
            System.out.printf("Успешно создана таблица %s.\n", figi);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertFigiData(){
        String insert_data = "INSERT INTO " + figi + " (close_price, time, volume) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(db_url); PreparedStatement preparedState = conn.prepareStatement(insert_data)) {
            preparedState.setDouble(1, close_price);
            preparedState.setTimestamp(2, rebuildTimestamp());
            preparedState.setDouble(3, volume);
            int rowsInserted = preparedState.executeUpdate();
            if (rowsInserted > 0) {
                System.out.printf("Данные свечи успешно добавлены в таблицу %s.\n", figi);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    public void selectData(){
        try (Connection conn = DriverManager.getConnection(db_url);
            Statement state = conn.createStatement();) {
            ResultSet select_data = state.executeQuery("SELECT close_price, time FROM " + figi + " ORDER BY time");
            while (select_data.next()) {
                double closePrice = select_data.getDouble("close_price");
                java.sql.Timestamp time = select_data.getTimestamp("time");
                
                //time_compare.add(time);
                System.out.println("Close Price: " + closePrice + " " + time + " " + figi); //позже будет передавать в класс стратегий
                //splitDataByInterval(time, closePrice, interval, time_compare);
            }
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // private void splitDataByInterval(java.sql.Timestamp time, double closePrice, int interval, List<java.sql.Timestamp> time_compare){
    //         if(time_compare.size() == 1){System.out.println("Close Price: " + closePrice + " " + time + " " + figi + "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");}
    //         if(time_compare.size() >= 2){

    //             long result = time_compare.get(time_compare.size() - 1).getTime() - time_compare.get(time_compare.size() - 2).getTime();

    //             if(result >= (interval*60000) && result <= (interval*60000*2)){
                    
    //                 System.out.println("Close Price: " + closePrice + " " + time + " " + figi + " " + result); //позже будет передавать в класс стратегий
                    
                        
    //             }
    //         }
    // }
    
    
    public void deleteData(){
        try (Connection conn = DriverManager.getConnection(db_url); Statement state = conn.createStatement();) {
            String delete_data = String.format("DELETE FROM %s;", figi);
            state.execute(delete_data);
            System.out.println("Данные удалены из таблицы " + figi);
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}








