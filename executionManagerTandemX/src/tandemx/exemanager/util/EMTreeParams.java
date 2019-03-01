package tandemx.exemanager.util;

public class EMTreeParams {
    private Long waitBtwSessions;
    private Integer numberOfObservations;
    private Integer stepSize;
    private Integer minNumberOfSymbols;

    public EMTreeParams(Long waitBtwSessions, Integer numberOfObservations, Integer stepSize, Integer minNumberOfSymbols) {
        this.waitBtwSessions = waitBtwSessions;
        this.numberOfObservations = numberOfObservations;
        this.stepSize = stepSize;
        this.minNumberOfSymbols = minNumberOfSymbols;
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
}
