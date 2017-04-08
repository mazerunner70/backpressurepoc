package uk.wils.backpressure.jobgenerator;

import uk.wils.backpressure.util.RangedValueBuilder;

import java.util.Random;

/**
 * Created by William O'Hara on 12/03/17.
 */
public class JobGeneratorGaussian extends JobGenerator {


    public JobGeneratorGaussian(RangedValueBuilder duration, RangedValueBuilder interval) {
        super(duration, interval);
    }

}
