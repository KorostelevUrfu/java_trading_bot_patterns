package tinkoff;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import tinkoff.DataBase.HistoryDataBase;
import tinkoff.DataBase.StreamDataBase;



public class Main {

    public static void main(String[] args) {

        CandleStreamProcessor candleStreamProcessor = new CandleStreamProcessor(Api.getStockApi());

        StreamDataBase db = new StreamDataBase();

        HistoryDataBase hdb = new HistoryDataBase();

        Instruments instruments = new Instruments();


        //[BBG004730N88, BBG004730RP0, BBG004731032, TCS00A107T19, BBG004731354, BBG004RVFFC0, BBG004S684M6, BBG000000001]

        //db.createFigiTable();

        //hdb.getInsertHistoryData();
        //hdb.serachFiles();
        hdb.selectHistoryData("SBER", 60);
        //instruments.getFigiFromTicker();
        

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