package tinkoff;

import ru.tinkoff.piapi.core.InvestApi;

public class Api {
    private static InvestApi api_2;
    private static InvestApi api;

    public static InvestApi getStockApi(){
        api = InvestApi.create(DataStorage.token);
        return api;
    }

    //на данный момент создается новое подключение для нестримовых операций чтобы не трогать подключение которое будет использовать стрим 
    //Назначение этого подключения все время умирать и возраждаться снова
    public static InvestApi getUnaryApi(){
        api_2 = InvestApi.create(DataStorage.token);
        return api_2;
    }

    public static InvestApi killUnaryApi(){
        api_2.destroy(0);
        return api_2;
    }
    
}