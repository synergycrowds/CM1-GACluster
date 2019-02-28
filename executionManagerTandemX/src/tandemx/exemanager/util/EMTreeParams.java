package tandemx.exemanager.util;

public class EMTreeParams {
    private Long waitBtwSessions;
    private Integer numberOfObservations;
    private Integer stepSize;

    public EMTreeParams(Long waitBtwSessions, Integer numberOfObservations, Integer stepSize) {
        this.waitBtwSessions = waitBtwSessions;
        this.numberOfObservations = numberOfObservations;
        this.stepSize = stepSize;
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
}
