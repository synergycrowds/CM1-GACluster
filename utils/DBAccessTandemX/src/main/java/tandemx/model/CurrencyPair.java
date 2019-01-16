package tandemx.model;

import javax.persistence.*;

@Entity
@Table(name = "CURRENCY_PAIR")
public class CurrencyPair {
    @Id
    @Column(name = "currency_pair_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "left_symbol_id")
    private Integer leftSymbolId;

    @Column(name = "right_symbol_id")
    private Integer rightSymbolId;

    public CurrencyPair() {
    }

    public CurrencyPair(Integer leftSymbolId, Integer rightSymbolId) {
        this.leftSymbolId = leftSymbolId;
        this.rightSymbolId = rightSymbolId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
