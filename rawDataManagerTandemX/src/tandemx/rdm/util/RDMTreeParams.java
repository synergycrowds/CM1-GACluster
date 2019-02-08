package tandemx.rdm.util;

public class RDMTreeParams {
    private long waitBtwSessions;

    public RDMTreeParams(long waitBtwSessions) {
        this.waitBtwSessions = waitBtwSessions;
    }

    public long getWaitBtwSessions() {
        return waitBtwSessions;
    }

    public void setWaitBtwSessions(long waitBtwSessions) {
        this.waitBtwSessions = waitBtwSessions;
    }
}
