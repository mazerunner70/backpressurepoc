package uk.wils.backpressure.jobgenerator;

/**
 * Typical job, has a certain time taken to process, and will use storage for a certain length of time
 * Created by vagrant on 12/03/17.
 */
public class Job {

    private int durationMillis;

    private int retentionTimeMillis;

    public Job(int durationMillis, int retentionTimeMillis) {
        this.durationMillis = durationMillis;
        this.retentionTimeMillis = retentionTimeMillis;
    }

    public int getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(int durationMillis) {
        this.durationMillis = durationMillis;
    }

    public int getRetentionTimeMillis() {
        return retentionTimeMillis;
    }

    public void setRetentionTimeMillis(int retentionTimeMillis) {
        this.retentionTimeMillis = retentionTimeMillis;
    }
}
