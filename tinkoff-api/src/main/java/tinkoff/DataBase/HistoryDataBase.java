package tinkoff.DataBase;

import java.time.Instant;
import java.util.Collection;
import java.util.Enumeration;

import java.io.File;
import java.util.zip.*;

import com.google.protobuf.Timestamp;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import tinkoff.DataStorage;
import tinkoff.Instruments;


public class HistoryDataBase extends StreamDataBase{

public File path = DataStorage.path;

private Instruments instruments = new Instruments();

//метод для получения истоических данных с разницей в 24 часа.
//в будущем можно использовать для корректировки данных за небольшой промежуток
    // private void getInsertHistoryData(){
    //     for(String figi : figi_list){
    //     List<HistoricCandle> candles1min = Api.getStockApi().getMarketDataService()
    //     .getCandlesSync(figi, Instant.now().minus(4, ChronoUnit.DAYS), Instant.now().minus(3, ChronoUnit.DAYS),
    //     CandleInterval.CANDLE_INTERVAL_1_MIN);

    //         for(int i = 0; i < candles1min.size(); i++){
    //         double close_price = candles1min.get(i).getClose().getUnits() + candles1min.get(i).getClose().getNano() * Math.pow(10, -9);
    //         com.google.protobuf.Timestamp time = candles1min.get(i).getTime();
    //             super.figi = figi;
    //             super.close_price = close_price;
    //             super.time = time;

    //             insertFigiData();
    //         }   
    //     }
    // }
    
    //Бегаем по директории и находим zip архив котировок
    public void serachFiles() {
        Collection<String> instrument = instruments.getFigiFromTicker().values(); 
    
        // Проверяем, является ли путь директорией
        if (path.isDirectory()) {
            File[] directoryFiles = path.listFiles(); // Получаем список файлов в директории
    
            // роверяем, что файлы существуют
            if (directoryFiles != null) {
                for (String figi : instrument) {
                    //проходим по всем файлам в директории
                    for (File file : directoryFiles) {
                        //если имя файла содержит figi то обрабатываем его
                        if (file.getName().startsWith(figi.toUpperCase())) {
                            System.out.println("\n\n" + file.getName() + "\n\n");
                            String zipFilePath = file.getAbsolutePath();
                            System.out.println("\n\n" + zipFilePath + "\n\n");
                            try (ZipFile zipFile = new ZipFile(zipFilePath)) {
                                Enumeration<? extends ZipEntry> entries = zipFile.entries();
    
                                //проходим по всем записям в zip архиве
                                while (entries.hasMoreElements()) {
                                    ZipEntry entry = entries.nextElement();
                                    System.out.println("\n\n" + entry + "\n\n");
                                    //проверяем является ли файл .csv
                                    if (entry.getName().endsWith(".csv")) {
                                        System.out.println("\n\n" + "Содержимое файла: " + entry.getName() + "\n\n");
                                    

                                        //создаем таблицу
                                        super.figi = figi;
                                        createFigiTable();

                                        //вставляем данные из CSV в БД
                                        insertCsvData(zipFile, entry, figi);
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    //бегаем по самому zip архиву и в нем парсим csv файлики
    private void insertCsvData(ZipFile zipFile, ZipEntry entry, String figi) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] values = line.split(";");
                    
                //String uid = values[0];
                //double open = Double.parseDouble(values[2]);
                double close_price = Double.parseDouble(values[3]);
                //double high = Double.parseDouble(values[4]);
                //double low = Double.parseDouble(values[5]);
                int volume = Integer.parseInt(values[6]);

                String string_time = values[1];
                //yyyy-mm-dd hh:mm:ss 
                Instant instant = Instant.parse(string_time);
                //Конвертация в com.google.protobuf.Timestamp
                Timestamp time = Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();

                super.figi = figi;
                super.time = time;
                super.close_price = close_price;
                super.volume = volume;

                insertFigiData();
                    
                // данные выводятся в следующем порядке 
                // UID — идентификатор инструмента, 
                // UTC — дата и время начала свечи, 
                // open — цена открытия, 
                // close — цена закрытия, 
                // high — максимальная цена за интервал, 
                // low — минимальная цена за интервал, 
                // volume — объём в лотах.

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectHistoryData(String ticker, int interval){
        super.figi = instruments.getFigiFromTicker().get(ticker);
        selectData(interval);
    }
}



