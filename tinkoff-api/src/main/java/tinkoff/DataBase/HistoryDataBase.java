package tinkoff.DataBase;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import tinkoff.CreateToken;


public class HistoryDataBase extends StreamDataBase{
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
}

