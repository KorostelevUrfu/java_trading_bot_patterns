package tinkoff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import tinkoff.CandleStreamProcessor.CandleData;

public class DataBase {

    private final String db_url = String.format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s&ssl=%s",
        DataStorage.db_host, DataStorage.db_port, DataStorage.db_name, DataStorage.db_user, DataStorage.db_password, DataStorage.db_ssl);

    public void startDB() {
        try (Connection conn = DriverManager.getConnection(db_url);
             Statement state = conn.createStatement()) {

            createFigiTable(state, DataStorage.figiList);

            //deleteData(state, DataStorage.figiList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createFigiTable(Statement state, List<String> figiList) {
        for (String figi : figiList) {
            String table = String.format("CREATE TABLE IF NOT EXISTS %s (close_price REAL, time TIMESTAMP);", figi);
            try {
                state.execute(table);
                System.out.printf("Таблица успешно создана/существует для figi: %s.\n", figi);
            } catch (SQLException e) {
                System.err.printf("Ошибка при создании таблицы: %s\n", e.getMessage());
            }
        }
    }

    public void insertFigiData(CandleData candle) {
        String insertQuery = "INSERT INTO " + candle.getFigi() + " (close_price, time) VALUES (?, ?)";
    
        try (Connection conn = DriverManager.getConnection(db_url);
             PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
    
            preparedStatement.setDouble(1, candle.getClosePrice());
            preparedStatement.setTimestamp(2, new Timestamp(candle.getTime().getSeconds() * 1000));
    
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.printf("Данные свечи успешно добавлены в таблицу %s.\n", candle.getFigi());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(Statement state, List<String> figiList){
        for (String figi : figiList){
            String delete = String.format("DELETE FROM %s;", figi);
            
            try {
                state.execute(delete);
                System.out.printf("Данные удалены из таблицы %s.\n", figi);
            }catch(SQLException e) {
                System.err.printf("Ошибка при удалении данных из таблицы: %s\n", e.getMessage());
            }
        }
    }

    public void insertHistoricData(List<String> figiList){
        for(String figi: figiList){
            List<HistoricCandle> candles1min = CreateToken.getToken().getMarketDataService()
            .getCandlesSync(figi, Instant.now().minus(1, ChronoUnit.DAYS), Instant.now(),
            CandleInterval.CANDLE_INTERVAL_1_MIN);

            for(int i = 0; i < candles1min.size(); i++){
            double close_price = candles1min.get(i).getClose().getUnits() + candles1min.get(i).getClose().getNano() * Math.pow(10, -9);
            com.google.protobuf.Timestamp time = candles1min.get(i).getTime();

                String insert = "INSERT INTO " + figi + " (close_price, time) VALUES (?, ?)";

                try (Connection conn = DriverManager.getConnection(db_url);
                PreparedStatement preparedStatement = conn.prepareStatement(insert)) {
    
                preparedStatement.setDouble(1, close_price);
                preparedStatement.setTimestamp(2, new Timestamp(time.getSeconds() * 1000));
    
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.printf("Данные свечи успешно добавлены в таблицу %s.\n", figi);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            }
        }   
    }

    public void selectData(List<String> figiList){
        for(String figi: figiList){
            try (Connection conn = DriverManager.getConnection(db_url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT close_price FROM " + figi + " ORDER BY time;")) {

            while (rs.next()) {
               double closePrice = rs.getDouble("close_price");
               System.out.println("Close Price: " + closePrice + " " + figi);
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        }
    }  

}


