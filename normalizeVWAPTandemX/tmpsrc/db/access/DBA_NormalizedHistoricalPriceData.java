/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.dataNormalizer.db.access;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.db.util.DBConnect;
import util.dataNormalizer.PriceNormalizer;
import util.dataNormalizer.db.entity.ECPNormalizedPrice;

/**
 *
 * @author mircea
 */
public class DBA_NormalizedHistoricalPriceData {
//
//    public static Timestamp getOldestUpdateTimestamp(String tableName, int exchange_id, int currencypair_id) {
//        Timestamp retTmstp = null;
//
//        try {
//            ResultSet rs;
//            Statement query = DBConnect.getConnTOMarketdata().createStatement();
//            String sql = "SELECT max(timestamp) FROM " + tableName + " WHERE exchange_id=" + exchange_id + " AND currency_pair_id=" + currencypair_id;
//            rs = query.executeQuery(sql);
//            if (rs.isBeforeFirst()) {
//                rs.next();
//                retTmstp = rs.getTimestamp(1);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DBA_Exchange.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return retTmstp;
//    }
//
//    public static List<ExchangeCurrencyPairUpdateState> getLastUpdatesForAllExchangesMinute() {
//        ArrayList<ExchangeCurrencyPairUpdateState> retList = new ArrayList();
//
//        ResultSet ecps = getExchangesCombinedWithCurrencyPairs();
//        try {
//            if (ecps.isBeforeFirst()) {
//                while (ecps.next()) {
//                    int e = ecps.getInt(2);
//                    int l = ecps.getInt(3);
//                    int r = ecps.getInt(4);
//                    int p = ecps.getInt(5);
//
//                    Timestamp lastUpdateTmstp = getOldestUpdateTimestamp("histdata_price_minute", e, p);
//
//                    retList.add(new ExchangeCurrencyPairUpdateState(e, l, r, p, lastUpdateTmstp));
//                }
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DBA_NormalizedHistoricalPriceData.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return retList;
//    }
//
//    public static List<ExchangeCurrencyPairUpdateState> getLastUpdatesForAllExchangesHour() {
//        ArrayList<ExchangeCurrencyPairUpdateState> retList = new ArrayList();
//
//        ResultSet ecps = getExchangesCombinedWithCurrencyPairs();
//        try {
//            if (ecps.isBeforeFirst()) {
//                while (ecps.next()) {
//                    int e = ecps.getInt(2);
//                    int l = ecps.getInt(3);
//                    int r = ecps.getInt(4);
//                    int p = ecps.getInt(5);
//
//                    Timestamp lastUpdateTmstp = getOldestUpdateTimestamp("histdata_price_hour", e, p);
//
//                    retList.add(new ExchangeCurrencyPairUpdateState(e, l, r, p, lastUpdateTmstp));
//                }
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DBA_NormalizedHistoricalPriceData.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return retList;
//    }
//
//    public static List<ExchangeCurrencyPairUpdateState> getLastUpdatesForAllExchangesDay() {
//        ArrayList<ExchangeCurrencyPairUpdateState> retList = new ArrayList();
//
//        ResultSet ecps = getExchangesCombinedWithCurrencyPairs();
//        try {
//            if (ecps.isBeforeFirst()) {
//                while (ecps.next()) {
//                    int e = ecps.getInt(2);
//                    int l = ecps.getInt(3);
//                    int r = ecps.getInt(4);
//                    int p = ecps.getInt(5);
//
//                    Timestamp lastUpdateTmstp = getOldestUpdateTimestamp("histdata_price_day", e, p);
//
//                    retList.add(new ExchangeCurrencyPairUpdateState(e, l, r, p, lastUpdateTmstp));
//                }
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DBA_NormalizedHistoricalPriceData.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return retList;
//    }
//

    // 22.09.2017 todo: separate in bibl. this duplicates dataUpdater.db.access.DBA_HistoricalPriceData.getExchangesCombinedWithCurrencyPairs()
    private static ResultSet getExchangesCombinedWithCurrencyPairs() {
        ResultSet retResultSet = null;

        try {
            Statement query = DBConnect.getConnTOMarketdata().createStatement();
            String sql = "SELECT * FROM exchange_currencypair WHERE available=true";
//                    + "exchange_currencypair_id=33 OR exchange_currencypair_id=34 OR "
//                    + "exchange_currencypair_id=39 OR exchange_currencypair_id=21 or exchange_currencypair_id=35 OR "
//                    + "exchange_currencypair_id=13 or exchange_currencypair_id=14 or exchange_currencypair_id=15 OR "
//                    + "exchange_currencypair_id=18 or exchange_currencypair_id=20 OR exchange_currencypair_id=21 or "
//                    + "exchange_currencypair_id=23 OR exchange_currencypair_id=24 or exchange_currencypair_id=26 OR "
//                    + "exchange_currencypair_id=29 OR exchange_currencypair_id=24 ";
            retResultSet = query.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBA_NormalizedHistoricalPriceData.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retResultSet;
    }
//
//    // not needed soon
//    public static List<HistdataPrice> getLstOpenPriceForCurrencyPair(int currency_pair_id) {
//        ArrayList retLstHistDataPrice = new ArrayList<>();
//        ResultSet rsHistDataPrice = getOpenPriceForCurrencyPair(currency_pair_id);
//
//        try {
//            while (rsHistDataPrice.next()) {// 10.08.17 update with volumef and volumet
//                HistdataPrice p = new HistdataPrice(rsHistDataPrice.getInt(1), rsHistDataPrice.getInt(2), rsHistDataPrice.getInt(3), rsHistDataPrice.getTimestamp(4), rsHistDataPrice.getDouble(5), rsHistDataPrice.getDouble(6), rsHistDataPrice.getDouble(7), rsHistDataPrice.getDouble(8), rsHistDataPrice.getDouble(9), rsHistDataPrice.getDouble(9));
//                retLstHistDataPrice.add(p);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DBA_Exchange.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return retLstHistDataPrice;
//    }
//
//    private static ResultSet getOpenPriceForCurrencyPair(int currency_pair_id) {
//        ResultSet retResultSet = null;
//        try {
//            Statement query = DBConnect.getConnTOMarketdata().createStatement();
//            String sql = "SELECT * FROM histdata_price;";
//            retResultSet = query.executeQuery(sql);
//
//        } catch (SQLException ex) {
//            Logger.getLogger(DBA_Exchange.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return retResultSet;
//    }
//
//    private static ResultSet getHistData(int price_id, int exchange_id, int currency_pair_id) {
//        ResultSet retResultSet = null;
//        try {
//            Statement query = DBConnect.getConnTOMarketdata().createStatement();
//            String sql = "SELECT * FROM histdata_price WHERE histdata_price_id=" + price_id + " AND exchange_id=" + exchange_id + " AND currency_pair_id=" + currency_pair_id;
//            retResultSet = query.executeQuery(sql);
//
//        } catch (SQLException ex) {
//            Logger.getLogger(DBA_Exchange.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return retResultSet;
//
//    }
//
//    public static HistdataPrice getHistDataPrice(int price_id, int exchange_id, int currency_pair_id) {
//        HistdataPrice data = new HistdataPrice();
//        ResultSet rsHistDataPrice = getHistData(price_id, exchange_id, currency_pair_id);
//
//        try {
//            while (rsHistDataPrice.next()) {// 10.08.17 update with volumef and volumet
//                HistdataPrice p = new HistdataPrice(rsHistDataPrice.getInt(1), rsHistDataPrice.getInt(2), rsHistDataPrice.getInt(3), rsHistDataPrice.getTimestamp(4), rsHistDataPrice.getDouble(5), rsHistDataPrice.getDouble(6), rsHistDataPrice.getDouble(7), rsHistDataPrice.getDouble(8), rsHistDataPrice.getDouble(9), rsHistDataPrice.getDouble(9));
//                data = p;
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DBA_Exchange.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return data;
//
//    }
//
//    private static ResultSet getClosePriceForCurrencyPair(int currency_pair_id, int exchange_id) {
//        ResultSet retResultSet = null;
//
//        try {
//            Statement query = DBConnect.getConnTOMarketdata().createStatement();
//            String sql = "SELECT histdata_price_id, close,timestamp FROM histdata_price WHERE exchange_id=" + exchange_id + " AND currency_pair_id =" + currency_pair_id;
//            retResultSet = query.executeQuery(sql);
//        } catch (SQLException ex) {
//            Logger.getLogger(DBA_Exchange.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return retResultSet;
//    }
//
//    public static ValuesSet getLstClosePriceForCurrencyPair(int currency_pair_id, int exchange_id) throws SQLException {
//
//        //ArrayList retLstHistDataPrice = new ArrayList<>();
//        ResultSet rsHistDataPrice = getClosePriceForCurrencyPair(currency_pair_id, exchange_id);
//        ValuesSet vsPrices = new ValuesSet();
//        try {
//            while (rsHistDataPrice.next()) {
//                Values v = new Values(rsHistDataPrice.getTimestamp("timestamp"), rsHistDataPrice.getDouble("close"), rsHistDataPrice.getInt("histdata_price_id"));
//                vsPrices.put(v);
//
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DBA_Exchange.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return vsPrices;
//
//    }
//
//    private static ResultSet getClosePrice(int price_id) {
//        ResultSet retResultSet = null;
//        try {
//            Statement query = DBConnect.getConnTOMarketdata().createStatement();
//            String sql = "SELECT close FROM histdata_price WHERE histdata_price_id=" + price_id;
//            retResultSet = query.executeQuery(sql);
//
//        } catch (SQLException ex) {
//            Logger.getLogger(DBA_Exchange.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return retResultSet;
//    }
//
//    public static Double getClosePriceValue(int price_id) throws SQLException {
//        ArrayList retLstHistDataPrice = new ArrayList<>();
//        ResultSet rsHistDataPrice = getClosePrice(price_id);
//        try {
//            if (rsHistDataPrice.next()) {
//                return rsHistDataPrice.getDouble(1);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DBA_Exchange.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }

    public static ArrayList<ECPNormalizedPrice> getLstLastNOpenPrices(int exchange_id, int currency_pair_id, Timestamp tmstpEnd, int limit) {
        ArrayList<ECPNormalizedPrice> retLst = new ArrayList();
        ResultSet rs = null;

        long beginMillis = tmstpEnd.getTime();
        for (int i = limit; i >= 0; i--) {
            beginMillis -= (24 * 60 * 60 * 1000);
        }
        Timestamp tmstpBegin = new Timestamp(beginMillis);

        try {
            Statement query = DBConnect.getConnTOMarketdata().createStatement();
            String sql = "SELECT currency_pair_id, timestamp, open FROM histdata_price_hour WHERE exchange_id=" + exchange_id + " AND currency_pair_id=" + currency_pair_id
                    + " AND timestamp <= '" + tmstpEnd + "' AND timestamp >= '" + tmstpBegin + "' ORDER BY timestamp ASC";

            rs = query.executeQuery(sql);
            if (rs.isBeforeFirst()) {
                if (rs.getFetchSize() >= 0) {
                    while (rs.next()) {
                        retLst.add(new ECPNormalizedPrice(exchange_id, currency_pair_id, rs.getTimestamp(2), rs.getDouble(3)));
                    }

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBA_NormalizedHistoricalPriceData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retLst;
    }

    /**
     * This must be changed to get the data (exchange_currencypair_id) only for
     * a given execution_id (not ALL).
     *
     * @param tmstpEnd
     * @param tableName
     * @param limit
     * @return
     */
    public static ArrayList getLstNormalizedHistoricalPricesAllCurrencyPairs(Timestamp tmstpEnd, String tableName, int limit) {
        ArrayList<ArrayList<ECPNormalizedPrice>> retLst = new ArrayList();

        ResultSet ecps = getExchangesCombinedWithCurrencyPairs();
        try {
            if (ecps.isBeforeFirst()) {
                while (ecps.next()) {
                    int e = ecps.getInt(2);
                    int p = ecps.getInt(5);
                    ArrayList lstNormPrices = getLstLastNOpenPrices(e, p, tmstpEnd, limit);
                    PriceNormalizer.normalize(lstNormPrices, 100);
                    if (!PriceNormalizer.isAbnormal()) {
                        retLst.add(lstNormPrices);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBA_NormalizedHistoricalPriceData.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retLst;
    }

    public static void main(String[] args) {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        ArrayList<ECPNormalizedPrice> lst = getLstLastNOpenPrices(18, 40, t, 100);
        for (ECPNormalizedPrice np : lst) {
            System.out.println(np);
        }
        PriceNormalizer.normalize(lst, 100);
        System.out.println("----------------After normalization--------------");
        for (ECPNormalizedPrice np : lst) {
            System.out.println(np);
        }

        ArrayList<ArrayList<ECPNormalizedPrice>> llecpn = getLstNormalizedHistoricalPricesAllCurrencyPairs(t, "", 100);
        System.out.println("----------------ALL now--------------");
        for (int i = 0; i < llecpn.size(); i++) {
            ArrayList<ECPNormalizedPrice> lecpn = llecpn.get(i);

            for (int j = 0; j < lecpn.size(); j++) {
                System.out.println(lecpn.get(j));
            }
        }
    }

//        public static void main(String[] args){
////        List<ExchangeCurrencyPairUpdateState> l = getLastUpdatesForAllExchanges();
////        for(ExchangeCurrencyPairUpdateState ecpus:l){
////            System.out.println(l);
////        }
//    }
}
