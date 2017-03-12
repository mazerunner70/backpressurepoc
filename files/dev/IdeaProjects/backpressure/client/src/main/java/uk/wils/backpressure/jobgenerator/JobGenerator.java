package uk.wils.backpressure.jobgenerator;

/**
 * Created by vagrant on 12/03/17.
 */
public interface JobGenerator {

    Job generate();

    void setDuration(int durationMilliseconds);

    void setDuration68PercentRange(double rangeMilliseconds);

    void setRetentionPeriod(int retentionPeriodMilliseconds);

    void setRetention68PercentRange(int retentionPeriodRangeMilliseconds);
}
