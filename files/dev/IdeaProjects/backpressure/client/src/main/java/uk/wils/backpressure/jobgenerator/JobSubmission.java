package uk.wils.backpressure.jobgenerator;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by William O'Hara on 08/04/17.
 */
public class JobSubmission implements Delayed {
    private long activateTime;

    private Job job = null;

    public JobSubmission(long activateTime, Job job) {
        this.activateTime = activateTime;
        this.job = job;
    }

    public Job getJob() {
        return job;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(activateTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
    }
}
