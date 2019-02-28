package tandemx.exemanager.create;

import tandemx.db.DBAExecutions;
import tandemx.db.DBAMarketData;
import tandemx.model.*;

import java.time.LocalDate;
import java.util.*;

public class ExecutionCreator {
    private DBAMarketData dbaMarketData;
    private DBAExecutions dbaExecutions;
    private int numberOfObservations;
    private int stepSize;

    public ExecutionCreator(DBAMarketData dbaMarketData, DBAExecutions dbaExecutions, int numberOfObservations, int stepSize) {
        this.dbaMarketData = dbaMarketData;
        this.dbaExecutions = dbaExecutions;
        this.numberOfObservations = numberOfObservations;
        this.stepSize = stepSize;
    }

    public void createExecutionsWhilePossible() {
        NormalizedStatus normalizedStatus = dbaMarketData.getNormalizedStatus();
        if (normalizedStatus == null || normalizedStatus.getLastDate() == null) {
            return;
        }
        LocalDate lastNormalizedDate = normalizedStatus.getLastDate().toLocalDate();
        Execution latestExecution = dbaExecutions.getLatestExecution();
        LocalDate latestExecutionDate;
        if (latestExecution != null) {
            latestExecutionDate = latestExecution.getDataTimestampEnd();
        } else {
            HistdataPriceDay earliestHistdataPriceDay = dbaMarketData.getEarliestHistdataPriceDay();
            if (earliestHistdataPriceDay == null || earliestHistdataPriceDay.getTimestamp() == null) {
                return;
            }
            latestExecutionDate = earliestHistdataPriceDay.getTimestamp().minusDays(stepSize).plusDays(numberOfObservations - 1);
        }
        LocalDate newDataTimestampEnd = latestExecutionDate.plusDays(stepSize);
        if (newDataTimestampEnd.isAfter(lastNormalizedDate)) {
            return;
        }
        List<CurrencyPair> currencyPairs = dbaMarketData.getCurrencyPairs();
        Map<Integer, List<CurrencyPair>> refSymb2CP = getReferenceSymbolIdToCurrencyPairs(currencyPairs);
        LocalDate newDataTimestampBegin;
        // TODO: 2/28/2019 check if it would be more efficient to create executions by ref symbol, rather than day (might check less intervals for zero volumes)
        while(!newDataTimestampEnd.isAfter(lastNormalizedDate)) {
            newDataTimestampBegin = newDataTimestampEnd.minusDays(numberOfObservations - 1);
            for (Integer refSymbolId: refSymb2CP.keySet()) {
                createExecutionIfPossible(refSymbolId, refSymb2CP.get(refSymbolId), newDataTimestampBegin,
                        newDataTimestampEnd);
            }
            newDataTimestampEnd = newDataTimestampEnd.plusDays(stepSize);
        }
    }

    private void createExecutionIfPossible(Integer refSymbolId, List<CurrencyPair> currencyPairs,
                                           LocalDate dataTimestampBegin, LocalDate dataTimestampEnd) {
        List<Integer> executionCurrencyPairIds = new ArrayList<>();
        for (CurrencyPair currencyPair: currencyPairs) {
            long numberOfNonZeroVolumeEntries =
                    dbaMarketData.getNumberOfHistdataPriceDaysWithVolumeAboveThreshold(currencyPair.getId(), 0,
                            dataTimestampBegin, dataTimestampEnd);
            if (numberOfObservations <= numberOfNonZeroVolumeEntries) {
                executionCurrencyPairIds.add(currencyPair.getId());
            }
        }
        if (executionCurrencyPairIds.size() > 0) {
            ExecutionDescription executionDescription =
                    new ExecutionDescription(dataTimestampBegin, dataTimestampEnd, refSymbolId, executionCurrencyPairIds);
            dbaExecutions.insertExecutionDescription(executionDescription);
        }
    }

    private Set<Integer> getReferenceSymbolIds(List<CurrencyPair> currencyPairs) {
        Set<Integer> referenceSymbolIds = new HashSet<>();
        currencyPairs.forEach(cp -> referenceSymbolIds.add(cp.getRightSymbolId()));
        return referenceSymbolIds;
    }

    private Map<Integer, Set<Integer>> getReferenceSymbolIdToSymbolsIds(List<CurrencyPair> currencyPairs) {
        Map<Integer, Set<Integer>> refSymb2Symb = new HashMap<>();
        for (CurrencyPair cp: currencyPairs) {
            if (!refSymb2Symb.containsKey(cp.getRightSymbolId())) {
                refSymb2Symb.put(cp.getRightSymbolId(), new HashSet<>());
            }
            refSymb2Symb.get(cp.getRightSymbolId()).add(cp.getLeftSymbolId());
        }
        return refSymb2Symb;
    }

    private Map<Integer, List<CurrencyPair>> getReferenceSymbolIdToCurrencyPairs(List<CurrencyPair> currencyPairs) {
        Map<Integer, List<CurrencyPair>> refSymb2CP = new HashMap<>();
        for (CurrencyPair cp: currencyPairs) {
            if (!refSymb2CP.containsKey(cp.getRightSymbolId())) {
                refSymb2CP.put(cp.getRightSymbolId(), new ArrayList<>());
            }
            refSymb2CP.get(cp.getRightSymbolId()).add(cp);
        }
        return refSymb2CP;
    }
}
