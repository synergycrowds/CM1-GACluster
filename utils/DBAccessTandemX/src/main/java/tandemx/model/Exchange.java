package tandemx.model;

import javax.persistence.*;

@Entity
@Table(name = "EXCHANGE")
public class Exchange {
    @Id
    @Column(name = "exchanges_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "displayname")
    private String displayName;

    public Exchange() {
    }

    public Exchange(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
