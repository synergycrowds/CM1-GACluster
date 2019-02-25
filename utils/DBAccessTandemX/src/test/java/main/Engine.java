package main;

import tandemx.db.DBAExecutions;
import tandemx.db.DBAExecutionsHib;
import tandemx.db.DBAMarketData;
import tandemx.db.DBAMarketDataHib;
import tandemx.db.util.Constants;
import tandemx.model.ExecutionDescription;
import tandemx.model.HistdataPriceDay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Engine {
    public static void main(String[] args) {
        (new Engine()).run();
    }

    private void run() {
//        runTestMarketData();
        runTestExecutions();
    }

    private void runTestMarketData() {
        DBAMarketData dbaMarketData = null;
        try {
            dbaMarketData = new DBAMarketDataHib(Constants.DB_NAME_BASE_MARKET_DATA_KAIKO);
//            HistdataPriceDay h = dbaMarketData.getLastNormalizedHistdataPriceDay(18, 234);
//            System.out.println(h);
//            List<HistdataPriceDay> hs = dbaMarketData.getHistDataPriceDayAfterTimestamp(18, 234, LocalDate.of(2018, 1, 10));
//            System.out.println(hs.size());
//            hs = dbaMarketData.getHistdataPriceDayForExchangeCurrencyPair(18, 234);
//            System.out.println(hs.size());
            System.out.println(dbaMarketData.getExchangeByName(Constants.AGGREGATE_EXCHANGE_NAME).getId());
        } finally {
            if (dbaMarketData != null) {
                dbaMarketData.close();
            }
        }
    }

    private void runTestExecutions() {
        DBAExecutions dbaExecutions = null;
        try {
            dbaExecutions = new DBAExecutionsHib(Constants.DB_NAME_BASE_EXECUTIONS);
            ExecutionDescription executionDescription = dbaExecutions.getOldestUncompletedExecutionDescription();
            System.out.println(executionDescription.getExecutionId());
            System.out.println(executionDescription.getDataTimestampBegin());
            System.out.println(executionDescription.getCurrencyPairIds().size());
            System.out.println(executionDescription.getCurrencyPairIds().get(0));
            dbaExecutions.updateExecutionCompletionTimestamps(executionDescription.getExecutionId(),
                    LocalDateTime.now(), LocalDateTime.now());
        } finally {
            if (dbaExecutions != null) {
                dbaExecutions.close();
            }
        }
    }
}
