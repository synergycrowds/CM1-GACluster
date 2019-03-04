package tandemx.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "NORMALIZED_STATUS")
public class NormalizedStatus {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "last_date")
    private LocalDateTime lastDate;

    public NormalizedStatus() {
    }

    public NormalizedStatus(LocalDateTime lastDate) {
        this.lastDate = lastDate;
    }

    public LocalDateTime getLastDate() {
        return lastDate;
    }

    public void setLastDate(LocalDateTime lastDate) {
        this.lastDate = lastDate;
    }
}
