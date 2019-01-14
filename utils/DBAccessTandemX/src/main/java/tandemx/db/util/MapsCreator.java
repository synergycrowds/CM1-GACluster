package tandemx.db.util;

import tandemx.model.CurrencyType;

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
}
