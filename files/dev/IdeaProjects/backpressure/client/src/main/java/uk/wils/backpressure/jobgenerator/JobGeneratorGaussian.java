package uk.wils.backpressure.jobgenerator;

import java.util.Random;

/**
 * Created by William O'Hara on 12/03/17.
 */
public class JobGeneratorGaussian implements JobGenerator {
    private int durationMilliseconds;
    private int rangeMilliseconds;
    private int retentionPeriodMilliseconds;
    private int retentionPeriodRangeMilliseconds;

    private Random random = new Random();

    @Override
    public Job generate() {
        int duration = (int) (durationMilliseconds + rangeMilliseconds * random.nextGaussian());
        int range = (int) (retentionPeriodMilliseconds + retentionPeriodRangeMilliseconds * random.nextGaussian());
        return new Job(duration, range);
    }

    @Override
    public void setDuration(int durationMilliseconds) {

        this.durationMilliseconds = durationMilliseconds;
    }

    @Override
    public void setDuration68PercentRange(int rangeMilliseconds) {

        this.rangeMilliseconds = rangeMilliseconds;
    }

    @Override
    public void setRetentionPeriod(int retentionPeriodMilliseconds) {

        this.retentionPeriodMilliseconds = retentionPeriodMilliseconds;
    }

    @Override
    public void setRetention68PercentRange(int retentionPeriodRangeMilliseconds) {

        this.retentionPeriodRangeMilliseconds = retentionPeriodRangeMilliseconds;
    }


}
