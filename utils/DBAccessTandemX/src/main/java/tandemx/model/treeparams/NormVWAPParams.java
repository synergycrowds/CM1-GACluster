package tandemx.model.treeparams;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NORMVWAPPARAMS")
public class NormVWAPParams {
    @Id
    @Column(name = "tree_id")
    private Integer treeId;

    @Column(name = "wait_btw_sessions")
    private Long waitBtwSessions;

    @Column(name = "min_volume_threshold")
    private Double minVolumeThreshold;

    public NormVWAPParams() {
    }

    public Integer getTreeId() {
        return treeId;
    }

    public void setTreeId(Integer treeId) {
        this.treeId = treeId;
    }

    public Long getWaitBtwSessions() {
        return waitBtwSessions;
    }

    public void setWaitBtwSessions(Long waitBtwSessions) {
        this.waitBtwSessions = waitBtwSessions;
    }

    public Double getMinVolumeThreshold() {
        return minVolumeThreshold;
    }

    public void setMinVolumeThreshold(Double minVolumeThreshold) {
        this.minVolumeThreshold = minVolumeThreshold;
    }
}
