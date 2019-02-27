package tandemx.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "EXECUTION")
public class Execution {
    @Id
    @Column(name = "execution_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_timestamp_begin")
    private LocalDate dataTimestampBegin;

    @Column(name = "data_timestamp_end")
    private LocalDate dataTimestampEnd;

    @Column(name = "execution_timestamp_begin")
    private LocalDateTime executionTimestampBegin;

    @Column(name = "execution_timestamp_end")
    private LocalDateTime executionTimestampEnd;

    @Column(name = "reference_symbol_id")
    private Integer referenceSymbolId;

    public Execution() {
    }

    public Execution(LocalDate dataTimestampBegin, LocalDate dataTimestampEnd, LocalDateTime executionTimestampBegin, LocalDateTime executionTimestampEnd, Integer referenceSymbolId) {
        this.dataTimestampBegin = dataTimestampBegin;
        this.dataTimestampEnd = dataTimestampEnd;
        this.executionTimestampBegin = executionTimestampBegin;
        this.executionTimestampEnd = executionTimestampEnd;
        this.referenceSymbolId = referenceSymbolId;
    }

    public Execution(LocalDate dataTimestampBegin, LocalDate dataTimestampEnd, Integer referenceSymbolId) {
        this.dataTimestampBegin = dataTimestampBegin;
        this.dataTimestampEnd = dataTimestampEnd;
        this.referenceSymbolId = referenceSymbolId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getExecutionTimestampBegin() {
        return executionTimestampBegin;
    }

    public void setExecutionTimestampBegin(LocalDateTime executionTimestampBegin) {
        this.executionTimestampBegin = executionTimestampBegin;
    }

    public LocalDateTime getExecutionTimestampEnd() {
        return executionTimestampEnd;
    }

    public void setExecutionTimestampEnd(LocalDateTime executionTimestampEnd) {
        this.executionTimestampEnd = executionTimestampEnd;
    }

    public Integer getReferenceSymbolId() {
        return referenceSymbolId;
    }

    public void setReferenceSymbolId(Integer referenceSymbolId) {
        this.referenceSymbolId = referenceSymbolId;
    }
}
