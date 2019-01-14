package tandemx.rdm.main;

import tandemx.db.DBAMarketData;
import tandemx.db.DBAMarketDataHib;
import tandemx.db.util.Constants;
import tandemx.db.util.MapsCreator;
import tandemx.model.Exchange;
import tandemx.model.Symbol;
import tandemx.rdm.datasource.KaikoCredentials;
import tandemx.rdm.datasource.KaikoHelper;

import java.util.List;
import java.util.Map;

public class Engine {
    public static void main(String[] args) {
        (new Engine()).run();
    }

    private void run() {
//        runGetAllExchanges();
        runGetAllSymbols();
    }

    private void runGetAllExchanges() {
        KaikoHelper kaiko = new KaikoHelper(KaikoCredentials.API_KEY);
        DBAMarketData dbaMarketData = null;
        try {
            List<Exchange> exchanges = kaiko.getExchanges();
            dbaMarketData = new DBAMarketDataHib(Constants.DB_NAME_BASE_MARKET_DATA_KAIKO);
            dbaMarketData.insertExchanges(exchanges);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbaMarketData != null) {
                dbaMarketData.close();
            }
        }
    }

    private void runGetAllSymbols() {
        KaikoHelper kaiko = new KaikoHelper(KaikoCredentials.API_KEY);
        DBAMarketData dbaMarketData = null;
        try {
            dbaMarketData = new DBAMarketDataHib(Constants.DB_NAME_BASE_MARKET_DATA_KAIKO);
            Map<String, Integer> currencyTypes = MapsCreator.createCurrencyTypeNameToId(dbaMarketData.getCurrencyTypes());
            List<Symbol> symbols = kaiko.getAssets(currencyTypes);
            dbaMarketData.insertSymbols(symbols);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbaMarketData != null) {
                dbaMarketData.close();
            }
        }
    }
}
