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

    public Execution() {
    }

    public Execution(LocalDate dataTimestampBegin, LocalDate dataTimestampEnd, LocalDateTime executionTimestampBegin, LocalDateTime executionTimestampEnd) {
        this.dataTimestampBegin = dataTimestampBegin;
        this.dataTimestampEnd = dataTimestampEnd;
        this.executionTimestampBegin = executionTimestampBegin;
        this.executionTimestampEnd = executionTimestampEnd;
    }

    public Execution(LocalDate dataTimestampBegin, LocalDate dataTimestampEnd) {
        this.dataTimestampBegin = dataTimestampBegin;
        this.dataTimestampEnd = dataTimestampEnd;
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
}
