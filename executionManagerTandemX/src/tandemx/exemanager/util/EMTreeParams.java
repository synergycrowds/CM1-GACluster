package tandemx.exemanager.util;

public class EMTreeParams {
    private Long waitBtwSessions;
    private Integer numberOfObservations;
    private Integer stepSize;
    private Integer minNumberOfSymbols;
    private Double volumeThreshold;
    private Double normPriceThreshold;

    public EMTreeParams(Long waitBtwSessions, Integer numberOfObservations, Integer stepSize, Integer minNumberOfSymbols, Double volumeThreshold, Double normPriceThreshold) {
        this.waitBtwSessions = waitBtwSessions;
        this.numberOfObservations = numberOfObservations;
        this.stepSize = stepSize;
        this.minNumberOfSymbols = minNumberOfSymbols;
        this.volumeThreshold = volumeThreshold;
        this.normPriceThreshold = normPriceThreshold;
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
