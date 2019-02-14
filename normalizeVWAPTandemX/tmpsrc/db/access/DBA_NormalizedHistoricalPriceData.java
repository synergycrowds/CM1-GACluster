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
