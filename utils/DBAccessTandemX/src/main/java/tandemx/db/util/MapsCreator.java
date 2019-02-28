package tandemx.db.util;

import tandemx.model.CurrencyPair;
import tandemx.model.CurrencyType;
import tandemx.model.Exchange;
import tandemx.model.Symbol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsCreator {
    /**
     * Create a mapping between currency types names and ids (map[name] = id)
     * @param currencyTypes list of currency types
     * @return the mapping
     */
    public static Map<String, Integer> createCurrencyTypeNameToId(List<CurrencyType> currencyTypes) {
        Map<String, Integer> name2id = new HashMap<>();
        for (CurrencyType currencyType: currencyTypes) {
            name2id.put(currencyType.getName(), currencyType.getId());
        }
        return name2id;
    }

    /**
     * Create a mapping between symbols names and ids (map[name] = id)
     * @param symbols list of symbols
     * @return the mapping
     */
    public static Map<String, Integer> createSymbolNameToId(List<Symbol> symbols) {
        Map<String, Integer> name2id = new HashMap<>();
        for (Symbol symbol: symbols) {
            name2id.put(symbol.getName(), symbol.getId());
        }
        return name2id;
    }

    /**
     * Create a mapping between exchange names and ids (map[name] = id)
     * @param exchanges list of exchanges
     * @return the mapping
     */
    public static Map<String, Integer> createExchangeNameToId(List<Exchange> exchanges) {
        Map<String, Integer> name2id = new HashMap<>();
        for (Exchange exchange: exchanges) {
            name2id.put(exchange.getName(), exchange.getId());
        }
        return name2id;
    }

    /**
     * Create a mapping between currency pair names and ids (map[leftSymbolName][rightSymbolName] = currencyPairId)
     * @param currencyPairs list of currency pairs
     * @param symbolsIdToName mapping between symbols names and ids
     * @return the mapping
     */
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

    /**
     * Create a mapping between currency pairs ids and instances of the class (map[id] = instance)
     * @param currencyPairs list of currency pairs
     * @return the mapping
     */
    public static Map<Integer, CurrencyPair> createCurrencyPairIdToInstance(List<CurrencyPair> currencyPairs) {
        Map<Integer, CurrencyPair> id2instance = new HashMap<>();
        for (CurrencyPair currencyPair: currencyPairs) {
            id2instance.put(currencyPair.getId(), currencyPair);
        }
        return id2instance;
    }

    /**
     * Create a mapping between symbols ids and instances of the class (map[id] = instance)
     * @param symbols list of symbols
     * @return the symbols IDs to instnaces mapping
     */
    public static Map<Integer, Symbol> createSymbolIdToInstance(List<Symbol> symbols) {
        Map<Integer, Symbol> id2instance = new HashMap<>();
        for (Symbol symbol: symbols) {
            id2instance.put(symbol.getId(), symbol);
        }
        return id2instance;
    }

    /**
     * Create a mapping between the IDs of the currency pair and the reference symbol (right symbol) of that pair
     * @param currencyPairs list of currency pairs
     * @param symbolsIdToInstance mapping between symbols IDs and instances
     * @return the currency pair ID to reference symbol mapping
     */
    public static Map<Integer, Symbol> createCurrencyPairIdToReferenceSymbol(List<CurrencyPair> currencyPairs,
                                                                                Map<Integer, Symbol> symbolsIdToInstance) {
        Map<Integer, Symbol> cpId2sId = new HashMap<>();
        for (CurrencyPair currencyPair: currencyPairs) {
            cpId2sId.put(currencyPair.getId(), symbolsIdToInstance.get(currencyPair.getRightSymbolId()));
        }
        return cpId2sId;
    }

    /**
     * Create a mapping between the IDs of the currency pair and the symbol (left symbol) of that pair
     * @param currencyPairs list of currency pairs
     * @param symbolsIdToInstance mapping between symbols IDs and instances
     * @return the currency pair ID to symbol mapping
     */
    public static Map<Integer, Symbol> createCurrencyPairIdToSymbol(List<CurrencyPair> currencyPairs,
                                                                             Map<Integer, Symbol> symbolsIdToInstance) {
        Map<Integer, Symbol> cpId2sId = new HashMap<>();
        for (CurrencyPair currencyPair: currencyPairs) {
            cpId2sId.put(currencyPair.getId(), symbolsIdToInstance.get(currencyPair.getLeftSymbolId()));
        }
        return cpId2sId;
    }

}
