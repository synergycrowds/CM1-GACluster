package tandemx.rdm.util;

import tandemx.model.CurrencyPair;
import tandemx.model.Exchange;
import tandemx.model.ExchangeCurrencyPair;
import tandemx.model.Symbol;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DataDifferences {
    /**
     * Get the list of symbols from a list which are not present in the map
     * @param symbolsMap mapping between symbols names and IDs
     * @param symbols list of symbols from which new symbols must be selected
     * @return the list of symbols from the list which are not in the map
     */
    public static List<Symbol> getNewSymbols(Map<String, Integer> symbolsMap, List<Symbol> symbols) {
        List<Symbol> newSymbols = new ArrayList<>();
        for (Symbol symbol: symbols) {
            if (!symbolsMap.keySet().contains(symbol.getName())) {
                newSymbols.add(symbol);
            }
        }
        return newSymbols;
    }

    /**
     * Get the list of exchanges from a list which are not present in the map
     * @param exchangesMap mapping between exchanges names and IDs
     * @param exchanges list of exchanges from which new exchanges must be selected
     * @return the list of exchanges from the list which are not in the map
     */
    public static List<Exchange> getNewExchanges(Map<String, Integer> exchangesMap, List<Exchange> exchanges) {
        List<Exchange> newExchanges = new ArrayList<>();
        for (Exchange exchange: exchanges) {
            if (!exchangesMap.keySet().contains(exchange.getName())) {
                newExchanges.add(exchange);
            }
        }
        return newExchanges;
    }

    /**
     * Get the list of currency pairs from the second list which are not present in the first list. As a side effect,
     * this method sorts both lists according to the IDs of the left symbols and then to the IDs of the right symbols.
     * @param currencyPairsFirst fist list of currency pairs
     * @param currencyPairsSecond second list of currency pairs, from which the pairs that are no present in the first list must be selected
     * @return a list of currency pairs which are in the second list, but not the first
     */
    public static List<CurrencyPair> getNewCurrencyPairs(List<CurrencyPair> currencyPairsFirst, List<CurrencyPair> currencyPairsSecond) {
        List<CurrencyPair> newCurrencyPairs = new ArrayList<>();
        Comparator<CurrencyPair> comp = (cp1, cp2) -> {
            if (cp1.getLeftSymbolId().equals(cp2.getLeftSymbolId())) {
                return cp1.getRightSymbolId().compareTo(cp2.getRightSymbolId());
            }
            return cp1.getLeftSymbolId().compareTo(cp2.getLeftSymbolId());
        };
        currencyPairsFirst.sort(comp);
        currencyPairsSecond.sort(comp);
        int i = 0;
        int j = 0;
        while (i < currencyPairsFirst.size() && j < currencyPairsSecond.size()) {
            int compVal = comp.compare(currencyPairsFirst.get(i), currencyPairsSecond.get(j));
            if (compVal == 0) {
                i++;
                j++;
            } else if (compVal < 0) {
                i++;
            } else {
                newCurrencyPairs.add(currencyPairsSecond.get(j));
                j++;
            }
        }
        // if the new currency pairs list was completely parsed, the remaining of the reference list is not important
        while (j < currencyPairsSecond.size()) {
            newCurrencyPairs.add(currencyPairsSecond.get(j));
            j++;
        }
        return newCurrencyPairs;
    }

    /**
     * Get the list of exchange-currency-pair combinations from the second list which are not present in the first list.
     * As a side effect, this method sorts both lists according to the IDs of the exchanges and then to the IDs of the
     * pairs.
     * @param exchangeCurrencyPairsFirst first list of exchange-currency-pair combinations
     * @param exchangeCurrencyPairsSecond second list of exchange-currency-pair combinations, from which the combinations that are no present in the first list must be selected
     * @return a list of exchange-currency-pair combinations which are in the second list, but not the first
     */
    public static List<ExchangeCurrencyPair> getNewExchangeCurrencyPairs(List<ExchangeCurrencyPair> exchangeCurrencyPairsFirst,
                                                                         List<ExchangeCurrencyPair> exchangeCurrencyPairsSecond) {
        List<ExchangeCurrencyPair> newExchangeCurrencyPairs = new ArrayList<>();
        Comparator<ExchangeCurrencyPair> comp = (ecp1, ecp2) -> {
            if (ecp1.getExchangeId().equals(ecp2.getExchangeId())) {
                return ecp1.getCurrencyPairId().compareTo(ecp2.getCurrencyPairId());
            }
            return ecp1.getExchangeId().compareTo(ecp2.getExchangeId());
        };
        exchangeCurrencyPairsFirst.sort(comp);
        exchangeCurrencyPairsSecond.sort(comp);
        int i = 0;
        int j = 0;
        while (i < exchangeCurrencyPairsFirst.size() && j < exchangeCurrencyPairsSecond.size()) {
            int compVal = comp.compare(exchangeCurrencyPairsFirst.get(i), exchangeCurrencyPairsSecond.get(j));
            if (compVal == 0) {
                i++;
                j++;
            } else if (compVal < 0) {
                i++;
            } else {
                newExchangeCurrencyPairs.add(exchangeCurrencyPairsSecond.get(j));
                j++;
            }
        }
        // if the new exchange currency pairs list was completely parsed, the remaining of the reference list is not important
        while (j < exchangeCurrencyPairsSecond.size()) {
            newExchangeCurrencyPairs.add(exchangeCurrencyPairsSecond.get(j));
            j++;
        }
        return newExchangeCurrencyPairs;
    }
}
