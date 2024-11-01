package tinkoff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.tinkoff.piapi.contract.v1.InstrumentShort;

public class Instruments {
    
    private Map<String,String> instrument_figis = new HashMap<String, String>();
    
    public Map<String,String> getFigiFromTicker(){
        for(String ticker : DataStorage.tickers){
            List<InstrumentShort> bb = Api.getUnaryApi().getInstrumentsService().findInstrumentSync(ticker);//создается список инструментов по тикеру но с разными параметрами
            for(int i = 0; i < bb.size(); i++){
                if(bb.get(i).getTicker().equals(ticker) && bb.get(i).getApiTradeAvailableFlag() == true){ //второе условие обязательно так как есть figi у которых такой же тикер но они не доступны для торговли через api
                    instrument_figis.put(ticker, bb.get(i).getFigi());
                    System.out.println(instrument_figis.values());
                    Api.killUnaryApi();//надо убивать соединение так как в первом цикле for мы его постоянно создаем и из за этого появляется ошибка некорректно закрытого соединения, короче харам создавать пока не закрыто

                    //вот полный ответ на запрос
                    //isin: "RU0009062467"
                    // figi: "BBG004S684M6"
                    // ticker: "SIBN"
                    // class_code: "TQBR"
                    // instrument_type: "share"
                    // name: "\320\223\320\260\320\267\320\277\321\200\320\276\320\274 \320\275\320\265\321\204\321\202\321\214"
                    // uid: "9ba367af-dfbd-4d9c-8730-4b1d5a47756e"
                    // position_uid: "fdf347d4-9262-474d-ae19-aedcd8c375a1"
                    // instrument_kind: INSTRUMENT_TYPE_SHARE
                    // api_trade_available_flag: true
                    // for_iis_flag: true
                    // first_1min_candle_date {
                    //   seconds: 1520447580
                    // }
                    // first_1day_candle_date {
                    //   seconds: 936576000
                    // }
                    // weekend_flag: true
                    // 31: 1
                }
            }
        }
        return instrument_figis;
    }
}
