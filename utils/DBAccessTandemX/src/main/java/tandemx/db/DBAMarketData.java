package tandemx.db;

import tandemx.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface DBAMarketData {
    /**
     * Method to be called when access to the database is no longer needed.
     * Method must be called to close connection to the database
     */
    void close();

    /**
     * Get the list of all exchanges
     * @return list of exchanges
     */
    List<Exchange> getExchanges();

    /**
     * Inserts exchanges in the database
     * @param exchanges collection of exchanges that will be stored
     * @return exchanges with ids set
     */
    Collection<Exchange> insertExchanges(Collection<Exchange> exchanges);

    /**
     * Get the list of all currency types
     * @return list of currency types
     */
    List<CurrencyType> getCurrencyTypes();

    /**
     * Inserts symbols in the database
     * @param symbols collection of exchanges that will be stored
     * @return symbols with ids set
     */
    Collection<Symbol> insertSymbols(Collection<Symbol> symbols);

    /**
     * Get the list of all symbols
     * @return list of symbols
     */
    List<Symbol> getSymbols();

    /**
     * Inserts currency pairs in the database
     * @param currencyPairs collection of currency pairs that will be stored
     * @return currency pairs with ids set
     */
    Collection<CurrencyPair> insertCurrencyPairs(Collection<CurrencyPair> currencyPairs);

    /**
     * Get the list of all currency pairs
     * @return list of currecy pairs
     */
    List<CurrencyPair> getCurrencyPairs();

    /**
     * Get the list of all exchange - currency pair combinations
     * @return list of exchange currency pairs
     */
    List<ExchangeCurrencyPair> getExchangeCurrencyPairs();

    /**
     * Inserts exchange - currency pair combinations in the database
     * @param exchangeCurrencyPairs collection of exchange - currency pair combinations that will be stored
     * @return exchange currency pairs with ids set
     */
    Collection<ExchangeCurrencyPair> insertExchangeCurrencyPairs(Collection<ExchangeCurrencyPair> exchangeCurrencyPairs);

    /**
     * Get the list of histdata_price_day entries
     * @return list of histdata_price_day entries
     */
    List<HistdataPriceDay> getHistdataPriceDays();

    /**
     * Inserts histdata_price_day entries in the database
     * @param histdataPriceDays collection of histdata_price_day entries that will be stored
     * @return ehistdata_price_day entries with ids set
     */
    Collection<HistdataPriceDay> insertHistdataPriceDays(Collection<HistdataPriceDay> histdataPriceDays);

    /**
     * Gets the most recent histdata_price_day entry. This entry is the one obtained by taking the first entry after
     * ordering descending after the timestamp
     * @return most recent entry; null if no extry exists in the database
     */
    HistdataPriceDay getMostRecentHistdataPriceDay();

    /**
     * Get the most recent (according to the timestamp field) histdata_price_day entry which has a non-null value for
     * the normalized price
     * @param exchangeId id of the exchange from which data is considered
     * @param currencyPairId id of currency pair for which data is considered
     * @return most recent histdata_price_day entry with non-null normalized price and the exchange id and currency pair id equal to the given ones; null if no such entry exists
     */
    HistdataPriceDay getLastNormalizedHistdataPriceDay(Integer exchangeId, Integer currencyPairId);

    /**
     * Get the list of histdata_price_day entries which have a later timestamp than a given one, ordered by the timestamp
     * @param exchangeId id of exchange from which data is considered
     * @param currencyPairId id of currency pair for which data is considered
     * @param timestamp timestamp which must be strictly earlier than the timestamps of any returned entries
     * @return list of histdata_price_day entries with the timestamp later than the given one and the exchange id and currency pair id equal to the given ones
     */
    List<HistdataPriceDay> getHistDataPriceDayAfterTimestamp(Integer exchangeId, Integer currencyPairId, LocalDate timestamp);

    /**
     * Get the list of histdata_price_day entries which have a certain exchange ID and currency pair ID, ordered by the timestamp
     * @param exchangeId id of exchange from which data is considered
     * @param currencyPairId id of currency pair for which data is considered
     * @return list of histdata_price_day entries with the exchange id and currency pair id equal to the given ones, ordered by timestamp
     */
    List<HistdataPriceDay> getHistdataPriceDayForExchangeCurrencyPair(Integer exchangeId, Integer currencyPairId);

    /**
     * Return an exchange which has the given name
     * @param name name of the exchange
     * @return an exchnage with that name; null if no such exchange exists
     */
    Exchange getExchangeByName(String name);

    /**
     * Update histdata_price_day entries which already exist in the DB
     * @param histdataPriceDayList list of entries to be updated
     */
    void updateHistDataPriceDayList(List<HistdataPriceDay> histdataPriceDayList);

    /**
     * Get histdata_price_day entries cor a currency pair which have the timestamp within a range. Entries are ordered
     * by timestamp
     * @param beginTimestamp beginning of the time range (included)
     * @param endTimestamp ending of the time range (included)
     * @param currencyPairId ID of the currencyPair
     * @return histdata_price_day entries
     */
    List<HistdataPriceDay> getHistdataPriceDaysTimeRangeCurrencyPair(LocalDate beginTimestamp, LocalDate endTimestamp,
                                                                     Integer currencyPairId);

    /**
     * Update the normalized status flag with the new value for last date
     * @param lastDate new value for last date
     */
    void updateNormalizedStatus(LocalDateTime lastDate);

    /**
     * Get the normalized status flag
     * @return the flag if if exists, null otherwise
     */
    NormalizedStatus getNormalizedStatus();

    /**
     * Get the histdata_price_day entry with the earliest timestamp
     * @return the earliest entry; null if no such entry exists
     */
    HistdataPriceDay getEarliestHistdataPriceDay();

    /**
     * Get the number of histdata_price_day entries which have the volume above a threshold. Only entries of a currency
     * pair within a time range are considered.
     * @param currencyPairId IDof the currency pair considered
     * @param volumeThreshold threshold for volume (excluded)
     * @param timestampBegin earliest timestamp considered (included)
     * @param timestampEnd latest timestamp considered (included)
     * @return number of entries which satisfy the requirements
     */
    long getNumberOfHistdataPriceDaysWithVolumeAboveThreshold(Integer currencyPairId, double volumeThreshold,
                                                              LocalDate timestampBegin, LocalDate timestampEnd);

    /**
     * Update symbols in the DB
     * @param symbols list of updated symbols
     */
    void updateSymbols(List<Symbol> symbols);

    /**
     * Get the number of histdata_price_day entries which have the volume above a threshold and the normalized price
     * under a threshold. Only entries of a currency pair within a time range are considered.
     * @param currencyPairId IDof the currency pair considered
     * @param normThreshold threshold for normalized price (excluded)
     * @param volumeThreshold threshold for volume (excluded)
     * @param timestampBegin earliest timestamp considered (included)
     * @param timestampEnd latest timestamp considered (included)
     * @return number of entries which satisfy the requirements
     */
    long getNumberOfHistdataPriceDaysWithVolumeAndNormPrice(Integer currencyPairId, double volumeThreshold,
                                                            double normThreshold, LocalDate timestampBegin,
                                                            LocalDate timestampEnd);
}
