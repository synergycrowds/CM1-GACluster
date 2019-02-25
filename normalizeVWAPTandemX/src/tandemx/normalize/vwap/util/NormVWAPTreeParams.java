package tandemx.normalize.vwap.util;

public class NormVWAPTreeParams {
    private Long waitBtwSessions;
    private Double minVolumeThreshold;

    public NormVWAPTreeParams(Long waitBtwSessions, Double minVolumeThreshold) {
        this.waitBtwSessions = waitBtwSessions;
        this.minVolumeThreshold = minVolumeThreshold;
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
