package tandemx.exemanager.create;

import tandemx.db.DBAExecutions;
import tandemx.db.DBAMarketData;
import tandemx.exemanager.pricenorm.PriceNormalizer;
import tandemx.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ExecutionCreator {
    private DBAMarketData dbaMarketData;
    private DBAExecutions dbaExecutions;
    private int numberOfObservations;
    private int stepSize;
    private int minNumberOfSymbols;
    private double volumeThreshold;
    private double normPriceThreshold;

    public ExecutionCreator(DBAMarketData dbaMarketData, DBAExecutions dbaExecutions, int numberOfObservations,
                            int stepSize, int minNumberOfSymbols, double volumeThreshold, double normPriceThreshold) {
        this.dbaMarketData = dbaMarketData;
        this.dbaExecutions = dbaExecutions;
        this.numberOfObservations = numberOfObservations;
        this.stepSize = stepSize;
        this.minNumberOfSymbols = minNumberOfSymbols;
        this.volumeThreshold = volumeThreshold;
        this.normPriceThreshold = normPriceThreshold;
    }

    /**
     * Create executions while data is available
     */
    public void createExecutionsWhilePossible() {
        HistdataPriceDay mostRecentHDPD = dbaMarketData.getMostRecentHistdataPriceDay();
        if (mostRecentHDPD == null || mostRecentHDPD.getTimestamp() == null) {
            return;
        }
        LocalDate lastDateForWhichDataIsAvailable = mostRecentHDPD.getTimestamp();
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
        if (newDataTimestampEnd.isAfter(lastDateForWhichDataIsAvailable)) {
            return;
        }
        List<CurrencyPair> currencyPairs = dbaMarketData.getCurrencyPairs();
        Map<Integer, List<CurrencyPair>> refSymb2CP = getReferenceSymbolIdToCurrencyPairs(currencyPairs);
        if (latestExecution != null) {
            checkLastExecutionDate(latestExecution.getDataTimestampEnd(), refSymb2CP);
        }
        LocalDate newDataTimestampBegin;
        // TODO: 2/28/2019 check if it would be more efficient to create executions by ref symbol, rather than day (might check less intervals for zero volumes)
        while(!newDataTimestampEnd.isAfter(lastDateForWhichDataIsAvailable)) {
            newDataTimestampBegin = newDataTimestampEnd.minusDays(numberOfObservations - 1);
            for (Integer refSymbolId: refSymb2CP.keySet()) {
                ExecutionDescription executionDescription =
                        createExecutionIfPossible(refSymbolId, refSymb2CP.get(refSymbolId), newDataTimestampBegin,
                        newDataTimestampEnd);
                if (executionDescription != null) {
                    dbaExecutions.insertExecutionDescription(executionDescription);
                }
            }
            newDataTimestampEnd = newDataTimestampEnd.plusDays(stepSize);
        }
    }

    /**
     * Create executions for a reference symbol while data is available
     * @param refSymbolId ID of the reference symbol
     * @param currencyPairs list of currency pairs for the reference symbol (currency pairs that have the given reference symbol)
     * @param dataTimestampBegin beginning of the period of time considered for the execution
     * @param dataTimestampEnd ending of the period of time considered for the execution
     * @return execution description if data is available; null otherwise
     */
    private ExecutionDescription createExecutionIfPossible(Integer refSymbolId, List<CurrencyPair> currencyPairs,
                                           LocalDate dataTimestampBegin, LocalDate dataTimestampEnd) {
        List<Integer> executionCurrencyPairIds = new ArrayList<>();
        for (CurrencyPair currencyPair: currencyPairs) {
            long numberOfNonZeroVolumeEntries =
                    dbaMarketData.getNumberOfHistdataPriceDaysWithVolumeAboveThreshold(currencyPair.getId(),
                            volumeThreshold, dataTimestampBegin, dataTimestampEnd);
            if (numberOfObservations <= numberOfNonZeroVolumeEntries) {
                List<HistdataPriceDay> histdataPriceDays =
                        dbaMarketData.getHistdataPriceDaysTimeRangeCurrencyPair(dataTimestampBegin, dataTimestampEnd,
                                currencyPair.getId());
                PriceNormalizer.normalize(histdataPriceDays, 1, volumeThreshold);
                if (histdataPriceDays.stream().filter(h -> h.getNormalizedPrice() >= normPriceThreshold).count() <= 0) {
                    executionCurrencyPairIds.add(currencyPair.getId());
                }
            }
        }
        if (executionCurrencyPairIds.size() >= minNumberOfSymbols) {
            return new ExecutionDescription(dataTimestampBegin, dataTimestampEnd, refSymbolId, executionCurrencyPairIds);
        }
        return null;
    }

    /**
     * Check if new executions can be made for the last period of time considered
     * @param dataTimestampEnd ending of the last period of time considered for the execution
     * @param refSymb2CP mapping between reference symbol IDs and corresponding currency pairs
     */
    private void checkLastExecutionDate(LocalDate dataTimestampEnd, Map<Integer, List<CurrencyPair>> refSymb2CP) {
        LocalDate dataTimestampBegin = dataTimestampEnd.minusDays(numberOfObservations - 1);
        List<ExecutionDescription> executionDescriptions =
                dbaExecutions.getExecutionDescriptionsByDataTimestampEnd(dataTimestampEnd);
        Map<Integer, ExecutionDescription> refSymId2ExeDesc = executionDescriptions.stream()
                .collect(Collectors.toMap(ExecutionDescription::getReferenceSymbolId, ed -> ed));
        for (Integer refSymbolId: refSymb2CP.keySet()) {
            ExecutionDescription executionDescription =
                    createExecutionIfPossible(refSymbolId, refSymb2CP.get(refSymbolId), dataTimestampBegin,
                            dataTimestampEnd);
            if (executionDescription != null) {
                if (!refSymId2ExeDesc.containsKey(executionDescription.getReferenceSymbolId())) {
                    dbaExecutions.insertExecutionDescription(executionDescription);
                } else {
                    Set<Integer> newCurrencyPairIds =
                            new HashSet<>(executionDescription.getCurrencyPairIds());
                    newCurrencyPairIds.removeAll(
                            refSymId2ExeDesc.get(executionDescription.getReferenceSymbolId()).getCurrencyPairIds());
                    if (newCurrencyPairIds.size() > 0) {
                        dbaExecutions.insertExecutionCurrencyPairs(
                                newCurrencyPairIds.stream()
                                        .map(cpId -> new ExecutionCurrencyPair(
                                                refSymId2ExeDesc.get(
                                                        executionDescription.getReferenceSymbolId()).getExecutionId(),
                                                cpId))
                                        .collect(Collectors.toList()));
                    }
                }
            }
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

    /**
     * Get a mapping between reference symbol IDs and the corresponding list of currency pairs
     * @param currencyPairs list of currency pairs
     * @return mapping between reference symbol IDs and currency pairs
     */
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
