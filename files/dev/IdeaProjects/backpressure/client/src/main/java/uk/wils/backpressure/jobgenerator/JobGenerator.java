package uk.wils.backpressure.jobgenerator;

import uk.wils.backpressure.util.RangedValueBuilder;

import java.util.UUID;

/**
 * Created by William O'Hara on 12/03/17.
 */
public class JobGenerator {

    private RangedValueBuilder rangedValueBuilderDuration = null;
    private RangedValueBuilder rangedValueBuilderRetention = null;

    protected JobGenerator(RangedValueBuilder duration, RangedValueBuilder interval) {
        rangedValueBuilderDuration = duration;
        rangedValueBuilderRetention = interval;
    }

    public Job generate() {
        int duration = rangedValueBuilderDuration.generate();
        int range = rangedValueBuilderRetention.generate();
        return new Job(UUID.randomUUID().toString(), duration, range);
    }

    public RangedValueBuilder getRangedValueBuilderDuration() {
        return rangedValueBuilderDuration;
    }

    public RangedValueBuilder getRangedValueBuilderRetention() {
        return rangedValueBuilderRetention;
    }



}
