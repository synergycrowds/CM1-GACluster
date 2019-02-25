package tandemx.normalize.vwap.pricenorm;

import tandemx.db.DBAMarketData;
import tandemx.db.util.Constants;
import tandemx.model.CurrencyPair;
import tandemx.model.Exchange;
import tandemx.model.HistdataPriceDay;

import java.util.List;

public class NormalizationManager {
    private DBAMarketData dbaMarketData;
    private double minVolumeThreshold;

    public NormalizationManager(DBAMarketData dbaMarketData, double minVolumeThreshold) {
        this.dbaMarketData = dbaMarketData;
        this.minVolumeThreshold = minVolumeThreshold;
    }

    /**
     * Get all records from the DB which are not normalized and normalize them
     */
    public void normalizeAllRecords() {
        List<CurrencyPair> currencyPairs = dbaMarketData.getCurrencyPairs();
        Exchange aggregateExchange = dbaMarketData.getExchangeByName(Constants.AGGREGATE_EXCHANGE_NAME);
        if (aggregateExchange == null) {
            // TODO: 2/18/2019 throw exception?
            return;
        }
        for (CurrencyPair currencyPair: currencyPairs) {
            this.normalizeRecordsForCurrencyPair(currencyPair, aggregateExchange.getId());
        }
    }

    /**
     * Get all records from the DB for a exchange-currency-pair combination which are not normalized and normalize them
     * @param currencyPair currency pair to consider
     * @param exchangeId id of the exchange to consider
     */
    private void normalizeRecordsForCurrencyPair(CurrencyPair currencyPair, Integer exchangeId) {
        HistdataPriceDay lastNormalizedRecord = dbaMarketData.getLastNormalizedHistdataPriceDay(exchangeId, currencyPair.getId());
        List<HistdataPriceDay> histdataPriceDayRecords;
        double bottom;
        if (lastNormalizedRecord == null) {
            histdataPriceDayRecords = dbaMarketData.getHistdataPriceDayForExchangeCurrencyPair(exchangeId, currencyPair.getId());
            bottom = 1;
        } else {
            histdataPriceDayRecords = dbaMarketData.getHistDataPriceDayAfterTimestamp(exchangeId, currencyPair.getId(),
                    lastNormalizedRecord.getTimestamp());
            histdataPriceDayRecords.add(0, lastNormalizedRecord);
            bottom = lastNormalizedRecord.getNormalizedPrice();
        }
        PriceNormalizer.normalize(histdataPriceDayRecords, bottom, minVolumeThreshold);
        dbaMarketData.updateHistDataPriceDayList(histdataPriceDayRecords);
    }
}
