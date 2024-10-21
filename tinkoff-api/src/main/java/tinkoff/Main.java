package tinkoff;

import java.util.concurrent.CountDownLatch;

import tinkoff.DataBase.HistoryDataBase;
import tinkoff.DataBase.StreamDataBase;



public class Main {

    public static void main(String[] args) {

        CandleStreamProcessor candleStreamProcessor = new CandleStreamProcessor(CreateToken.getToken());

        StreamDataBase db = new StreamDataBase();

        HistoryDataBase hdb = new HistoryDataBase();
        
        db.createFigiTable();

        //hdb.getInsertHistoryData();

        //Обработка и вывод значений свечей
        //candleStreamProcessor.processMarketData(DataStorage.figiList);
        
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread was interrupted: " + e.getMessage());
        }
        
    }
}