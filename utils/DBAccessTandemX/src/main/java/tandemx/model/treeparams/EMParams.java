package tandemx.model.treeparams;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMPARAMS")
public class EMParams {
    @Id
    @Column(name = "tree_id")
    private Integer treeId;

    @Column(name = "wait_btw_sessions")
    private Long waitBtwSessions;

    @Column(name = "number_of_observations")
    private Integer numberOfObservations;

    @Column(name = "step_size")
    private Integer stepSize;

    @Column(name = "min_number_of_symbols")
    private Integer minNumberOfSymbols;

    @Column(name = "volume_threshold")
    private Double volumeThreshold;

    @Column(name = "norm_price_threshold")
    private Double normPriceThreshold;

    public EMParams() {
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

    public Integer getNumberOfObservations() {
        return numberOfObservations;
    }

    public void setNumberOfObservations(Integer numberOfObservations) {
        this.numberOfObservations = numberOfObservations;
    }

    public Integer getStepSize() {
        return stepSize;
    }

    public void setStepSize(Integer stepSize) {
        this.stepSize = stepSize;
    }

    public Integer getMinNumberOfSymbols() {
        return minNumberOfSymbols;
    }

    public void setMinNumberOfSymbols(Integer minNumberOfSymbols) {
        this.minNumberOfSymbols = minNumberOfSymbols;
    }

    public Double getVolumeThreshold() {
        return volumeThreshold;
    }

    public void setVolumeThreshold(Double volumeThreshold) {
        this.volumeThreshold = volumeThreshold;
    }

    public Double getNormPriceThreshold() {
        return normPriceThreshold;
    }

    public void setNormPriceThreshold(Double normPriceThreshold) {
        this.normPriceThreshold = normPriceThreshold;
    }
}
