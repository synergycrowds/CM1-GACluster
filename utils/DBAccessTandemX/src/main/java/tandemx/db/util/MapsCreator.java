package tandemx.db.util;

import tandemx.model.CurrencyPair;
import tandemx.model.CurrencyType;
import tandemx.model.Exchange;
import tandemx.model.Symbol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsCreator {
    public static Map<String, Integer> createCurrencyTypeNameToId(List<CurrencyType> currencyTypes) {
        Map<String, Integer> name2id = new HashMap<>();
        for (CurrencyType currencyType: currencyTypes) {
            name2id.put(currencyType.getName(), currencyType.getId());
        }
        return name2id;
    }

    public static Map<String, Integer> createSymbolNameToId(List<Symbol> symbols) {
        Map<String, Integer> name2id = new HashMap<>();
        for (Symbol symbol: symbols) {
            name2id.put(symbol.getName(), symbol.getId());
        }
        return name2id;
    }

    public static Map<String, Integer> createExchangeNameToId(List<Exchange> exchanges) {
        Map<String, Integer> name2id = new HashMap<>();
        for (Exchange exchange: exchanges) {
            name2id.put(exchange.getName(), exchange.getId());
        }
        return name2id;
    }

    public static Map<String, Map<String, Integer>> createCurrencyPairNamesToId(List<CurrencyPair> currencyPairs,
                                                                                Map<Integer, String> symbolsIdToName) {
        // TODO: 1/16/2019 maybe create a string -> int mapping by implementing a pair string representation
        Map<String, Map<String, Integer>> names2id = new HashMap<>();
        for (CurrencyPair currencyPair: currencyPairs) {
            String nameLeft = symbolsIdToName.get(currencyPair.getLeftSymbolId());
            String nameRight = symbolsIdToName.get(currencyPair.getRightSymbolId());
//            if (!names2id.containsKey(nameLeft)) {
//                names2id.put(nameLeft, new HashMap<>());
//            }
            names2id.computeIfAbsent(nameLeft, k -> new HashMap<>());
            names2id.get(nameLeft).put(nameRight, currencyPair.getId());
        }
        return names2id;
    }

    public static Map<Integer, CurrencyPair> createCurrencyPairIdToInstance(List<CurrencyPair> currencyPairs) {
        Map<Integer, CurrencyPair> id2instance = new HashMap<>();
        for (CurrencyPair currencyPair: currencyPairs) {
            id2instance.put(currencyPair.getId(), currencyPair);
        }
        return id2instance;
    }

}
