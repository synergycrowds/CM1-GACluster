package tandemx.db;

import tandemx.db.DBAMarketData;
import tandemx.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public Collection<Exchange> insertExchanges(Collection<Exchange> exchanges) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        exchanges.forEach(e -> manager.persist(e));
        manager.getTransaction().commit();
        manager.close();
        return exchanges;
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
    public Collection<Symbol> insertSymbols(Collection<Symbol> symbols) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        symbols.forEach(s -> manager.persist(s));
        manager.getTransaction().commit();
        manager.close();
        return symbols;
    }

    @Override
    public List<Symbol> getSymbols() {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<Symbol> symbols = (List<Symbol>) manager
                .createQuery("select s from Symbol s")
                .getResultList();

        manager.getTransaction().commit();
        manager.close();
        return symbols;
    }

    @Override
    public Collection<CurrencyPair> insertCurrencyPairs(Collection<CurrencyPair> currencyPairs) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        currencyPairs.forEach(cp -> manager.persist(cp));
        manager.getTransaction().commit();
        manager.close();
        return currencyPairs;
    }

    @Override
    public List<CurrencyPair> getCurrencyPairs() {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<CurrencyPair> currencyPairs = (List<CurrencyPair>) manager
                .createQuery("select cp from CurrencyPair cp")
                .getResultList();

        manager.getTransaction().commit();
        manager.close();
        return currencyPairs;
    }

    @Override
    public Collection<ExchangeCurrencyPair> insertExchangeCurrencyPairs(Collection<ExchangeCurrencyPair> exchangeCurrencyPairs) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        exchangeCurrencyPairs.forEach(ecp -> manager.persist(ecp));
        manager.getTransaction().commit();
        manager.close();
        return exchangeCurrencyPairs;
    }

    @Override
    public List<ExchangeCurrencyPair> getExchangeCurrencyPairs() {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<ExchangeCurrencyPair> exchangeCurrencyPairs = (List<ExchangeCurrencyPair>) manager
                .createQuery("select ecp from ExchangeCurrencyPair ecp")
                .getResultList();

        manager.getTransaction().commit();
        manager.close();
        return exchangeCurrencyPairs;
    }

    @Override
    public List<HistdataPriceDay> getHistdataPriceDays() {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<HistdataPriceDay> histdataPriceDays = (List<HistdataPriceDay>) manager
                .createQuery("select h from HistdataPriceDay h")
                .getResultList();

        manager.getTransaction().commit();
        manager.close();
        return histdataPriceDays;
    }

    @Override
    public Collection<HistdataPriceDay> insertHistdataPriceDays(Collection<HistdataPriceDay> histdataPriceDays) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        histdataPriceDays.forEach(h -> manager.persist(h));
        manager.getTransaction().commit();
        manager.close();
        return histdataPriceDays;
    }

    @Override
    public HistdataPriceDay getMostRecentHistdataPriceDay() {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<HistdataPriceDay> histdataPriceDays = (List<HistdataPriceDay>) manager
                .createQuery("select h from HistdataPriceDay h order by h.timestamp desc")
                .setMaxResults(1)
                .getResultList();

        HistdataPriceDay result;
        if (histdataPriceDays.size() <= 0) {
            result = null;
        } else {
            result = histdataPriceDays.get(0);
        }

        manager.getTransaction().commit();
        manager.close();
        return result;
    }

    @Override
    public HistdataPriceDay getLastNormalizedHistdataPriceDay(Integer exchangeId, Integer currencyPairId) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<HistdataPriceDay> histdataPriceDays = (List<HistdataPriceDay>) manager
                .createQuery("select h from HistdataPriceDay h where h.exchangeId = :excId and " +
                        "h.currencyPairId = :cpId and h.normalizedPrice != null order by h.timestamp desc")
                .setMaxResults(1)
                .setParameter("excId", exchangeId)
                .setParameter("cpId", currencyPairId)
                .getResultList();

        HistdataPriceDay result;
        if (histdataPriceDays.size() <= 0) {
            result = null;
        } else {
            result = histdataPriceDays.get(0);
        }

        manager.getTransaction().commit();
        manager.close();
        return result;
    }

    @Override
    public List<HistdataPriceDay> getHistDataPriceDayAfterTimestamp(Integer exchangeId, Integer currencyPairId, LocalDate timestamp) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<HistdataPriceDay> histdataPriceDays = (List<HistdataPriceDay>) manager
                .createQuery("select h from HistdataPriceDay h where h.exchangeId = :excId and  " +
                        "h.currencyPairId = :cpId and h.timestamp > :tmstp order by h.timestamp")
                .setParameter("excId", exchangeId)
                .setParameter("cpId", currencyPairId)
                .setParameter("tmstp", timestamp)
                .getResultList();

        manager.getTransaction().commit();
        manager.close();
        return histdataPriceDays;
    }

    @Override
    public List<HistdataPriceDay> getHistdataPriceDayForExchangeCurrencyPair(Integer exchangeId, Integer currencyPairId) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<HistdataPriceDay> histdataPriceDays = (List<HistdataPriceDay>) manager
                .createQuery("select h from HistdataPriceDay h where h.exchangeId = :excId and  " +
                        "h.currencyPairId = :cpId order by h.timestamp")
                .setParameter("excId", exchangeId)
                .setParameter("cpId", currencyPairId)
                .getResultList();

        manager.getTransaction().commit();
        manager.close();
        return histdataPriceDays;
    }

    @Override
    public Exchange getExchangeByName(String name) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<Exchange> exchanges = (List<Exchange>) manager
                .createQuery("select ex from Exchange ex where ex.name = :exName")
                .setMaxResults(1)
                .setParameter("exName", name)
                .getResultList();

        Exchange result;
        if (exchanges.size() <= 0) {
            result = null;
        } else {
            result = exchanges.get(0);
        }

        manager.getTransaction().commit();
        manager.close();
        return result;
    }

    @Override
    public void updateHistDataPriceDayList(List<HistdataPriceDay> histdataPriceDayList) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        histdataPriceDayList.forEach(h -> manager.merge(h));
        manager.getTransaction().commit();
        manager.close();
    }

    @Override
    public List<HistdataPriceDay> getHistdataPriceDaysTimeRangeCurrencyPair(LocalDate beginTimestamp, LocalDate endTimestamp, Integer currencyPairId) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<HistdataPriceDay> histdataPriceDays = (List<HistdataPriceDay>) manager
                .createQuery("select h from HistdataPriceDay h where h.currencyPairId = :cpId and h.timestamp " +
                        "between :beginTmstp and :endTmstp order by h.timestamp")
                .setParameter("cpId", currencyPairId)
                .setParameter("beginTmstp", beginTimestamp)
                .setParameter("endTmstp", endTimestamp)
                .getResultList();

        manager.getTransaction().commit();
        manager.close();
        return histdataPriceDays;
    }

    @Override
    public void updateNormalizedStatus(LocalDateTime lastDate) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<NormalizedStatus> statuses = (List<NormalizedStatus>) manager
                .createQuery("select ns from NormalizedStatus ns")
                .setMaxResults(1)
                .getResultList();

        NormalizedStatus normalizedStatus;
        if (statuses.size() <= 0) {
            normalizedStatus = new NormalizedStatus(lastDate);
            manager.persist(normalizedStatus);
        } else {
            normalizedStatus = statuses.get(0);
            normalizedStatus.setLastDate(lastDate);
        }

        manager.getTransaction().commit();
        manager.close();
    }

    @Override
    public NormalizedStatus getNormalizedStatus() {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<NormalizedStatus> statuses = (List<NormalizedStatus>) manager
                .createQuery("select ns from NormalizedStatus ns")
                .setMaxResults(1)
                .getResultList();

        NormalizedStatus result;
        if (statuses.size() > 0) {
            result = statuses.get(0);
        } else {
            result = null;
        }

        manager.getTransaction().commit();
        manager.close();
        return result;
    }

    @Override
    public HistdataPriceDay getEarliestHistdataPriceDay() {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        List<HistdataPriceDay> histdataPriceDays = (List<HistdataPriceDay>) manager
                .createQuery("select h from HistdataPriceDay h order by h.timestamp")
                .setMaxResults(1)
                .getResultList();

        HistdataPriceDay result;
        if (histdataPriceDays.size() <= 0) {
            result = null;
        } else {
            result = histdataPriceDays.get(0);
        }

        manager.getTransaction().commit();
        manager.close();
        return result;
    }

    @Override
    public long getNumberOfHistdataPriceDaysWithVolumeAboveThreshold(Integer currencyPairId, double volumeThreshold,
                                                                        LocalDate timestampBegin,
                                                                        LocalDate timestampEnd) {
        final EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        long result = ((List<Long>) manager
                .createQuery("select count(h) from HistdataPriceDay h where h.currencyPairId = :cpId and " +
                        "h.timestamp between :tmstpBegin and :tmstpEnd and h.volume > :vol")
                .setParameter("cpId", currencyPairId)
                .setParameter("vol", volumeThreshold)
                .setParameter("tmstpBegin", timestampBegin)
                .setParameter("tmstpEnd", timestampEnd)
                .setMaxResults(1)
                .getResultList())
                .get(0);

        manager.getTransaction().commit();
        manager.close();
        return result;
    }
}
