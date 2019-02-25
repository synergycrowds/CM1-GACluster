package tandemx.model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ExecutionDescription {
    private Integer executionId;
    private LocalDate dataTimestampBegin;
    private LocalDate dataTimestampEnd;
    private List<Integer> currencyPairIds;

    public ExecutionDescription(Integer executionId, LocalDate dataTimestampBegin, LocalDate dataTimestampEnd, List<Integer> currencyPairIds) {
        this.executionId = executionId;
        this.dataTimestampBegin = dataTimestampBegin;
        this.dataTimestampEnd = dataTimestampEnd;
        this.currencyPairIds = currencyPairIds;
    }

    public ExecutionDescription(Execution execution, List<ExecutionCurrencyPair> executionCurrencyPairs) {
        // TODO: 2/25/2019 throw exception if execution ID incompatibility is found?
        this.executionId = execution.getId();
        this.dataTimestampBegin = execution.getDataTimestampBegin();
        this.dataTimestampEnd = execution.getDataTimestampEnd();
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
}
