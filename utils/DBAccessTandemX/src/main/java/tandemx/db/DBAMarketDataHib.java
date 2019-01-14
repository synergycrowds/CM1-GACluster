package tandemx.db;

import tandemx.db.DBAMarketData;
import tandemx.model.CurrencyType;
import tandemx.model.Exchange;
import tandemx.model.Symbol;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class DBAMarketDataHib implements DBAMarketData {
    private EntityManagerFactory factory;

    public DBAMarketDataHib(String dbName) {
        this.factory = Persistence.createEntityManagerFactory(dbName);
    }

    /**
     * Method to be called when access to the database is no longer needed.
     * Method must be called to close connection to the database
     */
    public void close() {
        factory.close();
    }

    @Override
    public List<Exchange> getExchanges() {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<Exchange> exchanges = (List<Exchange>) manager
                .createQuery("select e from Exchange e")
                .getResultList();

        manager.getTransaction().commit();
        manager.close();
        return exchanges;
    }

    @Override
    public void insertExchanges(Collection<Exchange> exchanges) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        exchanges.forEach(e -> manager.persist(e));
        manager.getTransaction().commit();
        manager.close();
    }

    @Override
    public List<CurrencyType> getCurrencyTypes() {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<CurrencyType> currencyTypes = (List<CurrencyType>) manager
                .createQuery("select ct from CurrencyType ct")
                .getResultList();

        manager.getTransaction().commit();
        manager.close();
        return currencyTypes;
    }

    @Override
    public void insertSymbols(Collection<Symbol> symbols) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        symbols.forEach(s -> manager.persist(s));
        manager.getTransaction().commit();
        manager.close();
    }
}
