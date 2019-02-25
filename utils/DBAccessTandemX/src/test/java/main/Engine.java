package main;

import tandemx.db.DBAMarketData;
import tandemx.db.DBAMarketDataHib;
import tandemx.db.util.Constants;
import tandemx.model.HistdataPriceDay;

import java.time.LocalDate;
import java.util.List;

public class Engine {
    public static void main(String[] args) {
        (new Engine()).run();
    }

    private void run() {
        DBAMarketData dbaMarketData = null;
        try {
            dbaMarketData = new DBAMarketDataHib(Constants.DB_NAME_BASE_MARKET_DATA_KAIKO);
//            HistdataPriceDay h = dbaMarketData.getLastNormalizedHistdataPriceDay(18, 234);
//            System.out.println(h);
//            List<HistdataPriceDay> hs = dbaMarketData.getHistDataPriceDayAfterTimestamp(18, 234, LocalDate.of(2018, 1, 10));
//            System.out.println(hs.size());
//            hs = dbaMarketData.getHistdataPriceDayForExchangeCurrencyPair(18, 234);
//            System.out.println(hs.size());
            System.out.println(dbaMarketData.getExchangeByName(Constants.AGGREGATE_EXCHANGE_NAME).getId());
        } finally {
            if (dbaMarketData != null) {
                dbaMarketData.close();
            }
        }
    }
}
