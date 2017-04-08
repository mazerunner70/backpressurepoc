package uk.wils.backpressure.util;

import java.util.Random;

/**
 * Created by William O'Hara on 25/03/17.
 */
public class GaussianRangedValueBuilder implements RangedValueBuilder{

    public static final double[] STD_FRACTIONS = new double[] {0.682689492137086, 0.954499736103642, 0.997300203936740, 0.999936657516334, 0.999999426696856, 0.999999998026825, 0.999999999997440};
    public static final int STD_LIMIT = 3;

    private int average;
    private int range68Percent;

    private int range100Percent;
    private Random random;

    public GaussianRangedValueBuilder(int average, int range68Percent) {
        this.average = average;
        this.range68Percent = range68Percent;
    }

    public GaussianRangedValueBuilder() {
        random = new Random();
    }

    public enum Generator {GAUSSIAN}

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public int getRange68Percent() {
        return range68Percent;
    }

    public int getRange100Percent() {
        return range100Percent;
    }

    public void setRange68Percent(int range68Percent) {
        this.range68Percent = range68Percent;
        this.range100Percent = (int) (range68Percent/STD_FRACTIONS[0]);
    }

    public int generate() {
        double gaussianValue = 0;
        while (Math.abs(gaussianValue)>STD_FRACTIONS[STD_LIMIT-1]) {
            gaussianValue = random.nextGaussian();
        }
        return average + (int)(gaussianValue * range100Percent);
    }

    public static GaussianRangedValueBuilder gaussianWithInts(int average, int range68Percent) {
        GaussianRangedValueBuilder gaussianRangedValueBuilder = new GaussianRangedValueBuilder();
        gaussianRangedValueBuilder.setAverage(average);
        gaussianRangedValueBuilder.setRange68Percent(range68Percent);
        return gaussianRangedValueBuilder;
    }


}
