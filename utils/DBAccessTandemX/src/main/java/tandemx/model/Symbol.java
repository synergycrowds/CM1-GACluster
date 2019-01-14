package tandemx.model;

import javax.persistence.*;

@Entity
@Table(name = "SYMBOL")
public class Symbol {
    @Id
    @Column(name = "symbol_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "currency_type")
    private Integer currencyTypeId;

    @Column(name = "logo")
    private String logo;

    public Symbol() {
    }

    public Symbol(String name, Integer currencyTypeId, String logo) {
        this.name = name;
        this.currencyTypeId = currencyTypeId;
        this.logo = logo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCurrencyTypeId() {
        return currencyTypeId;
    }

    public void setCurrencyTypeId(Integer currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
