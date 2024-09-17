package tinkoff;

import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.MarketDataStreamService;
import ru.tinkoff.piapi.core.stream.StreamProcessor;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;

import java.util.List;
import java.util.function.Consumer;
import com.google.protobuf.Timestamp;

// Класс для обработки рыночных данных
public class CandleStreamProcessor {

    private final MarketDataStreamService marketDataStream;
    DataBase db = new DataBase();

    public CandleStreamProcessor(InvestApi api) {
        this.marketDataStream = api.getMarketDataStreamService();
    }

    // Метод для обработки рыночных данных
    public void processMarketData(List<String> figiList) {
        StreamProcessor<MarketDataResponse> processor = response -> {
            if (response.hasCandle()) {

                CandleData candleData = new CandleDataBuilder()
                        .setFigi(response.getCandle().getFigi())
                        .setClosePrice(response.getCandle().getClose().getUnits() + response.getCandle().getClose().getNano() * Math.pow(10, -9))
                        .setTime(response.getCandle().getTime())
                        .setVolume(response.getCandle().getVolume())
                        .build();


                handleCandleData(candleData);

                // запись значений candleData в бд
                db.insertFigiData(candleData);

                
            }
        };

        Consumer<Throwable> onErrorCallback = error -> System.err.println("Ошибка: " + error.toString());

        // Подписка на поток свечей
        marketDataStream.newStream("candles_stream", processor, onErrorCallback).subscribeCandles(figiList, true);
        
    }


    public void handleCandleData(CandleData candleData) {
        System.out.println(candleData);
    }

    public static class CandleData {
        private final String figi;
        private final double closePrice;
        private final Timestamp time;
        private final long volume;

        public CandleData(String figi, double closePrice, Timestamp time, long volume) {
            this.figi = figi;
            this.closePrice = closePrice;
            this.time = time;
            this.volume = volume;
        }

        public String getFigi() {
            return figi;
        }

        public double getClosePrice() {
            return closePrice;
        }

        public Timestamp getTime() {
            return time;
        }
        public long getVolume(){
            return volume;
        }

        @Override
        public String toString() {
            return "CandleData{" + "\n" +
                    "figi=" + figi + "\n" +
                    "closePrice=" + closePrice + "\n" +
                    "time=" + time + "\n" +
                    "volume=" + volume +
                    "}";
        }
    }

    // Паттерн Builder
    public static class CandleDataBuilder {
        private String figi;
        private double closePrice;
        private Timestamp time;
        private long volume;

        public CandleDataBuilder setFigi(String figi) {
            this.figi = figi;
            return this;
        }

        public CandleDataBuilder setClosePrice(double closePrice) {
            this.closePrice = closePrice;
            return this;
        }

        public CandleDataBuilder setTime(Timestamp time) {
            this.time = time;
            return this; 
        }

        public CandleDataBuilder setVolume(long volume){
            this.volume = volume;
            return this;
        }

        public CandleData build() {
            return new CandleData(figi, closePrice, time, volume); 
        }
    }
}
