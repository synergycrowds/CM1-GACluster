package tandemx.model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ExecutionDescription {
    private Integer executionId;
    private LocalDate dataTimestampBegin;
    private LocalDate dataTimestampEnd;
    private Integer referenceSymbolId;
    private List<Integer> currencyPairIds;

    public ExecutionDescription(Integer executionId, LocalDate dataTimestampBegin, LocalDate dataTimestampEnd, List<Integer> currencyPairIds) {
        this.executionId = executionId;
        this.dataTimestampBegin = dataTimestampBegin;
        this.dataTimestampEnd = dataTimestampEnd;
        this.currencyPairIds = currencyPairIds;
    }

    public ExecutionDescription(LocalDate dataTimestampBegin, LocalDate dataTimestampEnd, Integer referenceSymbolId, List<Integer> currencyPairIds) {
        this.dataTimestampBegin = dataTimestampBegin;
        this.dataTimestampEnd = dataTimestampEnd;
        this.referenceSymbolId = referenceSymbolId;
        this.currencyPairIds = currencyPairIds;
    }

    public ExecutionDescription(Integer executionId, LocalDate dataTimestampBegin, LocalDate dataTimestampEnd, Integer referenceSymbolId, List<Integer> currencyPairIds) {
        this.executionId = executionId;
        this.dataTimestampBegin = dataTimestampBegin;
        this.dataTimestampEnd = dataTimestampEnd;
        this.referenceSymbolId = referenceSymbolId;
        this.currencyPairIds = currencyPairIds;
    }

    public ExecutionDescription(Execution execution, List<ExecutionCurrencyPair> executionCurrencyPairs) {
        // TODO: 2/25/2019 throw exception if execution ID incompatibility is found?
        this.executionId = execution.getId();
        this.dataTimestampBegin = execution.getDataTimestampBegin();
        this.dataTimestampEnd = execution.getDataTimestampEnd();
        this.referenceSymbolId = execution.getReferenceSymbolId();
        this.currencyPairIds = executionCurrencyPairs.stream()
                .map(ExecutionCurrencyPair::getCurrencyPairId).collect(Collectors.toList());
    }

    public LocalDate getDataTimestampBegin() {
        return dataTimestampBegin;
    }

    public void setDataTimestampBegin(LocalDate dataTimestampBegin) {
        this.dataTimestampBegin = dataTimestampBegin;
    }

    public LocalDate getDataTimestampEnd() {
        return dataTimestampEnd;
    }

    public void setDataTimestampEnd(LocalDate dataTimestampEnd) {
        this.dataTimestampEnd = dataTimestampEnd;
    }

    public List<Integer> getCurrencyPairIds() {
        return currencyPairIds;
    }

    public void setCurrencyPairIds(List<Integer> currencyPairIds) {
        this.currencyPairIds = currencyPairIds;
    }

    public Integer getExecutionId() {
        return executionId;
    }

    public void setExecutionId(Integer executionId) {
        this.executionId = executionId;
    }

    public Integer getReferenceSymbolId() {
        return referenceSymbolId;
    }

    public void setReferenceSymbolId(Integer referenceSymbolId) {
        this.referenceSymbolId = referenceSymbolId;
    }

    /**
     * Get the corresponding execution
     * @return the execution
     */
    public Execution getExecution() {
        return new Execution(dataTimestampBegin, dataTimestampEnd, referenceSymbolId);
    }

    /**
     * Get the corresponding ExecutionCurrencyPair instances. Observation: the executionId should be set, otherwise the
     * ExecutionCurrencyPair instances will have a null executionId.
     * @return the ExecutionCurrencyPairs
     */
    public List<ExecutionCurrencyPair> getExecutionCurrencyPairs() {
        return this.currencyPairIds.stream()
                .map(cpId -> new ExecutionCurrencyPair(executionId, cpId)).collect(Collectors.toList());
    }
}
