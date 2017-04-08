package uk.wils.backpressure.util;

/**
 * Created by William O'Hara on 03/04/17.
 */
public interface RangedValueBuilder {

    int generate();

    int getAverage();

    int getRange68Percent();

}
