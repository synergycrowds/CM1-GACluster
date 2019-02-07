package tandemx.rdm.obtain;

import tandemx.db.DBAMarketData;
import tandemx.db.util.Constants;
import tandemx.db.util.MapsCreator;
import tandemx.model.*;
import tandemx.rdm.datasource.KaikoHelper;
import tandemx.rdm.datasource.model.CountOhlcvVwap;
import tandemx.rdm.datasource.model.Instrument;
import tandemx.rdm.util.DataDifferences;
import tandemx.rdm.util.Pair;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class MarketDataObtainer {
    private DBAMarketData dbaMarketData;
    private KaikoHelper kaiko;

    public MarketDataObtainer(DBAMarketData dbaMarketData, KaikoHelper kaiko) {
        this.dbaMarketData = dbaMarketData;
        this.kaiko = kaiko;
    }

    /**
     * Update data from the DB with data from Kaiko. Symbols, exchanges, currency pairs and exchange-currency-pair
     * combinations are updated, and then for each currency pair, aggregate data is computed across all exchanges, for
     * each day from the last day in the db to the given day.
     * @param endDate first day for which data must not be computed
     * @throws Exception if there are problems during the computations (problems accessing Kaiko or the DB)
     */
    public void obtain(LocalDate endDate) throws Exception {
        Map<String, Integer> symbolsName2Id = getSymbolsMap();
        Map<String, Integer> exchangesName2Id = getExchangesMap();
        List<CurrencyPair> currencyPairs = dbaMarketData.getCurrencyPairs();
        List<ExchangeCurrencyPair> exchangeCurrencyPairs = dbaMarketData.getExchangeCurrencyPairs();
        Map<Integer, String> symbolsId2Name = symbolsName2Id.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        Map<Integer, List<Integer>> currencyPairsIdToExchangesIds =
                getCurrencyPairId2ExchangeIdsMap(currencyPairs, exchangeCurrencyPairs, symbolsName2Id, symbolsId2Name,
                        exchangesName2Id);
        Map<Integer, String> exchangesIdToName = exchangesName2Id.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        HistdataPriceDay mostRecentHistdataPriceDay = dbaMarketData.getMostRecentHistdataPriceDay();
        LocalDate startDate = null;
        if (mostRecentHistdataPriceDay != null) {
            startDate = mostRecentHistdataPriceDay.getTimestamp();
        }
        // TODO: 1/16/2019 search for better solution
        if (startDate == null) {
            startDate = LocalDate.of(2017, 12, 31);
        }
        startDate = startDate.plusDays(1);
        while (startDate.isBefore(endDate)) {
            List<HistdataPriceDay> histdataPriceDays = new ArrayList<>();
            LocalDate nextDay = startDate.plusDays(1);
            for (CurrencyPair currencyPair : currencyPairs) {
                histdataPriceDays.add(
                        getHistdataDayAggForCurrencyPair(currencyPair,
                                currencyPairsIdToExchangesIds.get(currencyPair.getId()),
                                exchangesIdToName, symbolsId2Name,
                                exchangesName2Id.get(Constants.AGGREGATE_EXCHANGE_NAME), startDate, nextDay));
            }
            // TODO: 2/6/2019 maybe insert imediately after computing each unit?
            dbaMarketData.insertHistdataPriceDays(histdataPriceDays);
            startDate = startDate.plusDays(1);
        }
    }

    /**
     * Compute aggregate data for a currency pair
     * @param currencyPair currency pair
     * @param exchangesIds list of exchanges from which to take data about the currency pair
     * @param exchangesIdToName mapping between exchanges IDs and names
     * @param symbolsId2Name mapping between symbols IDs and names
     * @param aggregateExchangeId ID of the virtual exchange which is used to store the aggregate data
     * @param currentDay first day for which the data should be aggregated
     * @param nextDay first day that should not be considered for the aggregated data
     * @return aggregated data of a currency pair
     * @throws Exception if there are problems when obtaining the data (problems connecting to Kaiko or the DB)
     */
    private HistdataPriceDay getHistdataDayAggForCurrencyPair(CurrencyPair currencyPair,
                                                              List<Integer> exchangesIds,
                                                              Map<Integer, String> exchangesIdToName,
                                                              Map<Integer, String> symbolsId2Name,
                                                              Integer aggregateExchangeId,
                                                              LocalDate currentDay, LocalDate nextDay) throws Exception {
        Double totalVolume = 0.0;
        Double totalPrices = 0.0;
        CountOhlcvVwap countOhlcvVwap = null;
        LocalDateTime startMoment = currentDay.atStartOfDay();
        LocalDateTime endMoment = nextDay.atStartOfDay();
        for (Integer exchangeId: exchangesIds) {
             countOhlcvVwap =
                    kaiko.getCountOhlcvVwapFromExchange(exchangesIdToName.get(exchangeId),
                            symbolsId2Name.get(currencyPair.getLeftSymbolId()),
                            symbolsId2Name.get(currencyPair.getRightSymbolId()), startMoment, endMoment);
            if (countOhlcvVwap != null) {
                totalPrices += countOhlcvVwap.getPrice() * countOhlcvVwap.getVolume();
                totalVolume += countOhlcvVwap.getVolume();
            }
        }
        if (totalVolume.compareTo(0.0) > 0) {
            totalPrices /= totalVolume;
        }
        LocalDate timestamp = startMoment.toLocalDate();
        if (countOhlcvVwap != null && countOhlcvVwap.getTimestamp() != null) {
            timestamp = Instant.ofEpochMilli(countOhlcvVwap.getTimestamp()).atZone(ZoneOffset.UTC).toLocalDate();
        }
        return new HistdataPriceDay(aggregateExchangeId, currencyPair.getId(), timestamp, totalPrices, totalVolume);
    }

    /**
     * Updates symbols from the DB with the ones from Kaiko. Creates and returns a mapping between symbols names and
     * IDs, for all symbols in the DB.
     * @return symbols names to IDs mapping
     * @throws Exception if there are problems when obtaining the data (problems connecting to Kaiko or the DB)
     */
    private Map<String, Integer> getSymbolsMap() throws Exception {
        Map<String, Integer> currencyTypesMap = MapsCreator.createCurrencyTypeNameToId(dbaMarketData.getCurrencyTypes());
        List<Symbol> symbols = kaiko.getAssets(currencyTypesMap);
        Map<String, Integer> symbolsMap = MapsCreator.createSymbolNameToId(dbaMarketData.getSymbols());
        List<Symbol> newSymbols = DataDifferences.getNewSymbols(symbolsMap, symbols);
        if (newSymbols.size() > 0) {
            newSymbols = new ArrayList<>(dbaMarketData.insertSymbols(newSymbols));
            symbolsMap.putAll(MapsCreator.createSymbolNameToId(newSymbols));
        }
        return symbolsMap;
    }

    /**
     * Updates exchanges from the DB with the ones from Kaiko. Creates and returns a mapping between exchanges names and
     * IDs, for all exchanges in the DB.
     * @return exchanges names to IDs mapping
     * @throws Exception if there are problems when obtaining the data (problems connecting to Kaiko or the DB)
     */
    private Map<String, Integer> getExchangesMap() throws Exception {
        Map<String, Integer> exchangesMap = MapsCreator.createExchangeNameToId(dbaMarketData.getExchanges());
        List<Exchange> newExchanges = DataDifferences.getNewExchanges(exchangesMap, kaiko.getExchanges());
        if (newExchanges.size() > 0) {
            newExchanges = new ArrayList<>(dbaMarketData.insertExchanges(newExchanges));
            exchangesMap.putAll(MapsCreator.createExchangeNameToId(newExchanges));
        }
        return exchangesMap;
    }

    /**
     * Updates currency pairs and exchange-currency-pair combinations from the DB with the ones from Kaiko.
     * Creates and returns a mapping between currency pairs IDs and list of the IDs of the exchanges which offer these
     * pairs.
     * @param currencyPairs list of currency pairs which are already in the DB
     * @param exchangeCurrencyPairs list of exchange-currency-pair combinations which are already in the DB
     * @param symbolsNameToIdMap mapping between symbols names and IDs
     * @param symbolsIdToNameMap mapping between symbols IDs and names
     * @param exchangesNameToIdMap mapping between exchanges names and IDs
     * @return mapping between currency pairs IDs and list of exchanges IDs
     * @throws Exception if there are problems when obtaining the data (problems connecting to Kaiko or the DB)
     */
    private Map<Integer, List<Integer>> getCurrencyPairId2ExchangeIdsMap(List<CurrencyPair> currencyPairs,
                                                                          List<ExchangeCurrencyPair> exchangeCurrencyPairs,
                                                                          Map<String, Integer> symbolsNameToIdMap,
                                                                          Map<Integer, String> symbolsIdToNameMap,
                                                                          Map<String, Integer> exchangesNameToIdMap) throws Exception {
        List<Instrument> instruments = kaiko.getInstruments();
        List<CurrencyPair> kaikoCurrencyPairs = getCurrencyPairsFromInstruments(instruments, symbolsNameToIdMap);
        List<CurrencyPair> newCurrencyPairs = DataDifferences.getNewCurrencyPairs(currencyPairs, kaikoCurrencyPairs);
        if (newCurrencyPairs.size() > 0) {
            currencyPairs.addAll(dbaMarketData.insertCurrencyPairs(newCurrencyPairs));
        }
        Map<String, Map<String, Integer>> currencyPairsNamesToIdMap =
                MapsCreator.createCurrencyPairNamesToId(currencyPairs, symbolsIdToNameMap);
        List<ExchangeCurrencyPair> kaikoExchangeCurrencyPairs =
                getExchangeCurrencyPairsFromInstruments(instruments, exchangesNameToIdMap, currencyPairsNamesToIdMap,
                        symbolsNameToIdMap);
        List<ExchangeCurrencyPair> newExchangeCurrencyPairs =
                DataDifferences.getNewExchangeCurrencyPairs(exchangeCurrencyPairs, kaikoExchangeCurrencyPairs);
        if (newExchangeCurrencyPairs.size() > 0) {
            exchangeCurrencyPairs.addAll(dbaMarketData.insertExchangeCurrencyPairs(newExchangeCurrencyPairs));
        }
        Map<Integer, List<Integer>> currencyPairsIdToExchangeIdsMap = new HashMap<>();
        for (ExchangeCurrencyPair exchangeCurrencyPair: exchangeCurrencyPairs) {
            currencyPairsIdToExchangeIdsMap.computeIfAbsent(exchangeCurrencyPair.getCurrencyPairId(), cpId -> new ArrayList<>());
            currencyPairsIdToExchangeIdsMap.get(exchangeCurrencyPair.getCurrencyPairId()).add(exchangeCurrencyPair.getExchangeId());
        }
        return currencyPairsIdToExchangeIdsMap;
    }

    /**
     * Extract exchange-currency-pair combinations from instrumens.
     * @param instruments list of instruments
     * @param exchangesNameToIdMap mapping between exchanges names and IDs
     * @param currencyPairsNamesToIdMap mapping between currency pairs names and Ids (left symbol name first, right second)
     * @param symbolsNameToIdMap mapping between symbols names and IDs
     * @return list of exchange-currency-pair combinations
     */
    private List<ExchangeCurrencyPair> getExchangeCurrencyPairsFromInstruments(List<Instrument> instruments,
                                                                               Map<String, Integer> exchangesNameToIdMap,
                                                                               Map<String, Map<String, Integer>> currencyPairsNamesToIdMap,
                                                                               Map<String, Integer> symbolsNameToIdMap) {
        Set<String> combinations = new HashSet<>();
        List<ExchangeCurrencyPair> exchangeCurrencyPairs = new ArrayList<>();
        for (Instrument instrument: instruments) {
            String combinationCode = instrument.getExchangeCode() + "," + instrument.getBaseAsset() + "," + instrument.getQuoteAsset();
            // ignore duplicates in the instruments (even if they have different start and end timestamps)
            if (!combinations.contains(combinationCode)) {
                exchangeCurrencyPairs.add(
                        new ExchangeCurrencyPair(exchangesNameToIdMap.get(instrument.getExchangeCode()),
                                symbolsNameToIdMap.get(instrument.getBaseAsset()),
                                symbolsNameToIdMap.get(instrument.getQuoteAsset()),
                                currencyPairsNamesToIdMap.get(instrument.getBaseAsset()).get(instrument.getQuoteAsset()),
                                true));
                combinations.add(combinationCode);
            }
        }
        return exchangeCurrencyPairs;
    }

    /**
     * Extract currency pairs from instruments.
     * @param instruments list of instruments
     * @param symbolsNameToIdMap mapping between symbols names and IDs
     * @return list of currency pairs
     */
    private List<CurrencyPair> getCurrencyPairsFromInstruments(List<Instrument> instruments, Map<String, Integer> symbolsNameToIdMap) {
        List<Pair<String, String>> currencyNamePairs = new ArrayList<>();
        for (Instrument instrument: instruments) {
            currencyNamePairs.add(new Pair<>(instrument.getBaseAsset(), instrument.getQuoteAsset()));
        }
        currencyNamePairs.sort((p1, p2) -> {
            if (p1.getFirst().equals(p2.getFirst())) {
                return p1.getSecond().compareTo(p2.getSecond());
            } else {
                return p1.getFirst().compareTo(p2.getFirst());
            }
        });
        List<CurrencyPair> currencyPairs = new ArrayList<>();
        for (int i = 1; i < currencyNamePairs.size(); i++) {
            Pair<String, String> p1 = currencyNamePairs.get(i - 1);
            Pair<String, String> p2 = currencyNamePairs.get(i);
            if (!p1.getFirst().equals(p2.getFirst()) || !p1.getSecond().equals(p2.getSecond())) {
                currencyPairs.add(new CurrencyPair(symbolsNameToIdMap.get(p1.getFirst()), symbolsNameToIdMap.get(p1.getSecond())));
            }
        }
        Pair<String, String> p = currencyNamePairs.get(currencyNamePairs.size() - 1);
        currencyPairs.add(new CurrencyPair(symbolsNameToIdMap.get(p.getFirst()), symbolsNameToIdMap.get(p.getSecond())));
        return currencyPairs;
    }
}
