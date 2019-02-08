package tandemx.model.treeparams;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RDMPARAMS")
public class RDMParams {
    @Id
    @Column(name = "tree_id")
    private Integer treeId;

    @Column(name = "wait_btw_sessions")
    private Long waitBtwSessions;

    public RDMParams() {
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
}
