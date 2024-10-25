package tinkoff.DataBase;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;
import java.util.List;

import java.io.File;
import java.util.zip.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import tinkoff.CreateToken;
import tinkoff.DataStorage;


public class HistoryDataBase extends StreamDataBase{

public File path = DataStorage.path;


//добавить логику если используются методы HistoryDataBase, то нельзя пользоваться методами StreamDataBase так как изменяются/используются поля StreamDataBase
    public void getInsertHistoryData(){
        for(String figi : figi_list){
        List<HistoricCandle> candles1min = CreateToken.getToken().getMarketDataService()
        .getCandlesSync(figi, Instant.now().minus(4, ChronoUnit.DAYS), Instant.now().minus(3, ChronoUnit.DAYS),
        CandleInterval.CANDLE_INTERVAL_1_MIN);

            for(int i = 0; i < candles1min.size(); i++){
            double close_price = candles1min.get(i).getClose().getUnits() + candles1min.get(i).getClose().getNano() * Math.pow(10, -9);
            com.google.protobuf.Timestamp time = candles1min.get(i).getTime();
                super.figi = figi;
                super.close_price = close_price;
                super.time = time;

                insertFigiData();
            }   
        }
    }

    //Бегаем по директории и находим zip архив котировок
    public void serachFiles(){
        if(path.isDirectory()){
            File[] direcory_files = path.listFiles();
            for(File file : direcory_files){
                String zipFilePath = file.getAbsolutePath(); 
        
                try (ZipFile zipFile = new ZipFile(zipFilePath)) {
                    Enumeration<? extends ZipEntry> entries = zipFile.entries();
                    
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        
                        if (entry.getName().endsWith(".csv")) {
                            System.out.println("Содержимое файла: " + entry.getName());
                            printCsvContent(zipFile, entry);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //бегаем по самому zip архиву и в нем парсим csv файлики
    private static void printCsvContent(ZipFile zipFile, ZipEntry entry) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] values = line.split(";");
                for (String value : values) {
                    System.out.print(value.trim() + "\t");
                    // данные выводятся в следующем порядке 
                    // UID — идентификатор инструмента, 
                    // UTC — дата и время начала свечи, 
                    // open — цена открытия, 
                    // close — цена закрытия, 
                    // high — максимальная цена за интервал, 
                    // low — минимальная цена за интервал, 
                    // volume — объём в лотах.
                }
                System.out.println(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
