package tinkoff;

import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.MarketDataStreamService;
import ru.tinkoff.piapi.core.stream.StreamProcessor;
import tinkoff.DataBase.StreamDataBase;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;

import java.util.List;
import java.util.function.Consumer;

import com.google.protobuf.Timestamp;

public class CandleStreamProcessor {

    private final MarketDataStreamService marketDataStream;

    public CandleStreamProcessor(InvestApi api) {
        this.marketDataStream = api.getMarketDataStreamService();
    }

    // Метод для обработки рыночных данных
    public void processMarketData(List<String> figiList) {
        StreamProcessor<MarketDataResponse> processor = response -> {
            if (response.hasCandle()) {
                String figi_stream = response.getCandle().getFigi();
                double close_price = response.getCandle().getClose().getUnits() + response.getCandle().getClose().getNano() * Math.pow(10, -9);
                Timestamp time = response.getCandle().getTime();
                long volume = response.getCandle().getVolume();

                StreamDataBase db = new StreamDataBase(figi_stream, close_price, time, volume);
            
                //выводим данные свечи
                db.printCandleData();
                //запись данных свечи в БД
                db.insertFigiData();
            }
        };
        Consumer<Throwable> onErrorCallback = error -> System.err.println("Ошибка: " + error.toString());
        // Подписка на поток свечей
        marketDataStream.newStream("candles_stream", processor, onErrorCallback).subscribeCandles(figiList, true);
    }
}
