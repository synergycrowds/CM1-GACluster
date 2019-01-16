package tandemx.model;

import javax.persistence.*;

@Entity
@Table(name = "EXCHANGE_CURRENCYPAIR")
public class ExchangeCurrencyPair {
    @Id
    @Column(name = "exchange_currencypair_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "exchange_id")
    private Integer exchangeId;

    @Column(name = "left_symbol_id")
    private Integer leftSymbolId;

    @Column(name = "right_symbol_id")
    private Integer rightSymbolId;

    @Column(name = "currencypair_id")
    private Integer currencyPairId;

    @Column(name = "available")
    private Boolean available;

    public ExchangeCurrencyPair() {
    }

    public ExchangeCurrencyPair(Integer exchangeId, Integer leftSymbolId, Integer rightSymbolId, Integer currencyPairId, Boolean available) {
        this.exchangeId = exchangeId;
        this.leftSymbolId = leftSymbolId;
        this.rightSymbolId = rightSymbolId;
        this.currencyPairId = currencyPairId;
        this.available = available;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Integer getLeftSymbolId() {
        return leftSymbolId;
    }

    public void setLeftSymbolId(Integer leftSymbolId) {
        this.leftSymbolId = leftSymbolId;
    }

    public Integer getRightSymbolId() {
        return rightSymbolId;
    }

    public void setRightSymbolId(Integer rightSymbolId) {
        this.rightSymbolId = rightSymbolId;
    }

    public Integer getCurrencyPairId() {
        return currencyPairId;
    }

    public void setCurrencyPairId(Integer currencyPairId) {
        this.currencyPairId = currencyPairId;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
