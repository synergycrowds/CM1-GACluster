package tandemx.model;

import javax.persistence.*;

@Entity
@Table(name = "EXECUTION_CURRENCY_PAIR")
public class ExecutionCurrencyPair {
    @Id
    @Column(name = "execution_currency_pair_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "execution_id")
    private Integer executionId;

    @Column(name = "currency_pair_id")
    private Integer currencyPairId;

    public ExecutionCurrencyPair(Integer executionId, Integer currencyPairId) {
        this.executionId = executionId;
        this.currencyPairId = currencyPairId;
    }

    public ExecutionCurrencyPair() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExecutionId() {
        return executionId;
    }

    public void setExecutionId(Integer executionId) {
        this.executionId = executionId;
    }

    public Integer getCurrencyPairId() {
        return currencyPairId;
    }

    public void setCurrencyPairId(Integer currencyPairId) {
        this.currencyPairId = currencyPairId;
    }
}
