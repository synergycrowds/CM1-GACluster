package tandemx.model;

import javax.persistence.*;

@Entity
@Table(name = "CURRENCY_TYPE")
public class CurrencyType {
    @Id
    @Column(name = "currency_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    public CurrencyType() {
    }

    public CurrencyType(String name) {
        this.name = name;
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
}
