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

import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

public class MarketDataObtainer {
    private DBAMarketData dbaMarketData;
    private KaikoHelper kaiko;

    public MarketDataObtainer(DBAMarketData dbaMarketData, KaikoHelper kaiko) {
        this.dbaMarketData = dbaMarketData;
        this.kaiko = kaiko;
    }

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
        Map<Integer, CurrencyPair> currencyPairsIdToInstance = MapsCreator.createCurrencyPairIdToInstance(currencyPairs);
        Map<Integer, String> exchangesIdToName = exchangesName2Id.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        LocalDate startDate = dbaMarketData.getMostRecentHistdataPriceDay().getTimestamp();
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
                        getHistdataDayForCurrencyPair(currencyPair,
                                currencyPairsIdToExchangesIds.get(currencyPair.getId()),
                                exchangesIdToName, symbolsId2Name,
                                exchangesName2Id.get(Constants.AGGREGATE_EXCHANGE_NAME), startDate, nextDay));
            }
            dbaMarketData.insertHistdataPriceDays(histdataPriceDays);
            startDate = startDate.plusDays(1);
        }
    }

    private HistdataPriceDay getHistdataDayForCurrencyPair(CurrencyPair currencyPair,
                                                           List<Integer> exchangesIds,
                                                           Map<Integer, String> exchangesIdToName,
                                                           Map<Integer, String> symbolsId2Name,
                                                           Integer aggregateExchangeId,
                                                           LocalDate currentDay, LocalDate nextDay) {

        for (Integer exchangeId: exchangesIds) {
//            CountOhlcvVwap countOhlcvVwap = kaiko.getCountOhlcvVwapFromExchange()
        }
        return null;
    }

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

    private Map<String, Integer> getExchangesMap() throws Exception {
        Map<String, Integer> exchangesMap = MapsCreator.createExchangeNameToId(dbaMarketData.getExchanges());
        List<Exchange> newExchanges = DataDifferences.getNewExchanges(exchangesMap, kaiko.getExchanges());
        if (newExchanges.size() > 0) {
            newExchanges = new ArrayList<>(dbaMarketData.insertExchanges(newExchanges));
            exchangesMap.putAll(MapsCreator.createExchangeNameToId(newExchanges));
        }
        return exchangesMap;
    }

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

    private List<ExchangeCurrencyPair> getExchangeCurrencyPairsFromInstruments(List<Instrument> instruments,
                                                                               Map<String, Integer> exchangesNameToIdMap,
                                                                               Map<String, Map<String, Integer>> currencyPairsNamesToIdMap,
                                                                               Map<String, Integer> symbolsNameToIdMap) {
        // TODO: 1/16/2019 handle duplicates where timestamps differ?
        Set<String> combinations = new HashSet<>();
        List<ExchangeCurrencyPair> exchangeCurrencyPairs = new ArrayList<>();
        for (Instrument instrument: instruments) {
            String combinationCode = instrument.getExchangeCode() + "," + instrument.getBaseAsset() + "," + instrument.getQuoteAsset();
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
