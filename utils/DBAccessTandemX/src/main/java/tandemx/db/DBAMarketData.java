package tandemx.db;

import tandemx.model.CurrencyType;
import tandemx.model.Exchange;
import tandemx.model.Symbol;

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
     */
    void insertExchanges(Collection<Exchange> exchanges);

    /**
     * Get the list of all currency types
     * @return list of currency types
     */
    List<CurrencyType> getCurrencyTypes();

    /**
     * Inserts symbols in the database
     * @param symbols collection of exchanges that will be stored
     */
    void insertSymbols(Collection<Symbol> symbols);
}
