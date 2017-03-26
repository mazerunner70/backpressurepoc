package uk.wils.backpressure.util;

import org.junit.Test;
import uk.wils.backpressure.jobgenerator.JobGeneratorGaussianTest;

import static org.junit.Assert.*;

/**
 * Created by William O'Hara on 25/03/17.
 */
public class RangedValueBuilderTest {
    @Test
    public void getAverage() throws Exception {
        RangedValueBuilder rangedValueBuilder = RangedValueBuilder.gaussianWithInts(12, 20);
        assertEquals("average shold be equal 12", 12, rangedValueBuilder.getAverage());
    }

    @Test
    public void setAverage() throws Exception {
        RangedValueBuilder rangedValueBuilder = RangedValueBuilder.gaussianWithInts(12, 20);
        assertEquals("average shold be equal 12", 12, rangedValueBuilder.getAverage());
        rangedValueBuilder.setAverage(14);
        assertEquals("average shold be equal 14", 14, rangedValueBuilder.getAverage());
    }

    @Test
    public void getRange68Percent() throws Exception {
        RangedValueBuilder rangedValueBuilder = RangedValueBuilder.gaussianWithInts(12, 20);
        assertEquals("average shold be equal 20", 20, rangedValueBuilder.getRange68Percent());
    }

    @Test
    public void setRange68Percent() throws Exception {
        RangedValueBuilder rangedValueBuilder = RangedValueBuilder.gaussianWithInts(12, 20);
        assertEquals("average shold be equal 20", 20, rangedValueBuilder.getRange68Percent());
        assertEquals("range100percent should be 29", 29, rangedValueBuilder.getRange100Percent());
        rangedValueBuilder.setRange68Percent(124);
        assertEquals("average shold be equal 124", 124, rangedValueBuilder.getRange68Percent());
        assertEquals("range100percent should be 181", 181, rangedValueBuilder.getRange100Percent());
    }

    @Test
    public void gaussianWithInts() throws Exception {
        RangedValueBuilder rangedValueBuilder = RangedValueBuilder.gaussianWithInts(12, 20);
        assertEquals("average shold be equal 12", 12, rangedValueBuilder.getAverage());
        assertEquals("average shold be equal 20", 20, rangedValueBuilder.getRange68Percent());
    }

    @Test
    public void testGaussianGenerate() {
        RangedValueBuilder rangedValueBuilder = RangedValueBuilder.gaussianWithInts(120000, 2000);
        for (int f = 0 ; f < 200 ; ++f) {
            int value = rangedValueBuilder.generate();
            double tolerance = rangedValueBuilder.getRange68Percent()/ JobGeneratorGaussianTest.STD_FRACTIONS[0]* JobGeneratorGaussianTest.STD_FRACTIONS[2];
            double lowerBound = rangedValueBuilder.getAverage() - tolerance;
            double upperBound = rangedValueBuilder.getAverage() + tolerance;
            assertTrue("value "+value+" out of range, lower="+lowerBound+", upper="+upperBound, value >lowerBound && value < upperBound);
        }
    }


}