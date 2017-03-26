package uk.wils.backpressure.jobgenerator;

/**
 * Created by William O'Hara on 12/03/17.
 */
public interface JobGenerator {

    Job generate();

    void setDuration(int durationMilliseconds);

    void setDuration68PercentRange(int rangeMilliseconds);

    void setRetentionPeriod(int retentionPeriodMilliseconds);

    void setRetention68PercentRange(int retentionPeriodRangeMilliseconds);
}
