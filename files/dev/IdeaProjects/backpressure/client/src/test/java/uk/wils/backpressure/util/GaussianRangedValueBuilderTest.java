package uk.wils.backpressure.util;

import org.junit.Test;
import uk.wils.backpressure.jobgenerator.JobGeneratorGaussianTest;

import static org.junit.Assert.*;

/**
 * Created by William O'Hara on 25/03/17.
 */
public class GaussianRangedValueBuilderTest {
    @Test
    public void getAverage() throws Exception {
        GaussianRangedValueBuilder gaussianRangedValueBuilder = GaussianRangedValueBuilder.gaussianWithInts(12, 20);
        assertEquals("average shold be equal 12", 12, gaussianRangedValueBuilder.getAverage());
    }

    @Test
    public void setAverage() throws Exception {
        GaussianRangedValueBuilder gaussianRangedValueBuilder = GaussianRangedValueBuilder.gaussianWithInts(12, 20);
        assertEquals("average shold be equal 12", 12, gaussianRangedValueBuilder.getAverage());
        gaussianRangedValueBuilder.setAverage(14);
        assertEquals("average shold be equal 14", 14, gaussianRangedValueBuilder.getAverage());
    }

    @Test
    public void getRange68Percent() throws Exception {
        GaussianRangedValueBuilder gaussianRangedValueBuilder = GaussianRangedValueBuilder.gaussianWithInts(12, 20);
        assertEquals("average shold be equal 20", 20, gaussianRangedValueBuilder.getRange68Percent());
    }

    @Test
    public void setRange68Percent() throws Exception {
        GaussianRangedValueBuilder gaussianRangedValueBuilder = GaussianRangedValueBuilder.gaussianWithInts(12, 20);
        assertEquals("average shold be equal 20", 20, gaussianRangedValueBuilder.getRange68Percent());
        assertEquals("range100percent should be 29", 29, gaussianRangedValueBuilder.getRange100Percent());
        gaussianRangedValueBuilder.setRange68Percent(124);
        assertEquals("average shold be equal 124", 124, gaussianRangedValueBuilder.getRange68Percent());
        assertEquals("range100percent should be 181", 181, gaussianRangedValueBuilder.getRange100Percent());
    }

    @Test
    public void gaussianWithInts() throws Exception {
        GaussianRangedValueBuilder gaussianRangedValueBuilder = GaussianRangedValueBuilder.gaussianWithInts(12, 20);
        assertEquals("average shold be equal 12", 12, gaussianRangedValueBuilder.getAverage());
        assertEquals("average shold be equal 20", 20, gaussianRangedValueBuilder.getRange68Percent());
    }

    @Test
    public void testGaussianGenerate() {
        GaussianRangedValueBuilder gaussianRangedValueBuilder = GaussianRangedValueBuilder.gaussianWithInts(120000, 2000);
        for (int f = 0 ; f < 200 ; ++f) {
            int value = gaussianRangedValueBuilder.generate();
            double tolerance = gaussianRangedValueBuilder.getRange68Percent()/ JobGeneratorGaussianTest.STD_FRACTIONS[0]* JobGeneratorGaussianTest.STD_FRACTIONS[2];
            double lowerBound = gaussianRangedValueBuilder.getAverage() - tolerance;
            double upperBound = gaussianRangedValueBuilder.getAverage() + tolerance;
            assertTrue("value "+value+" out of range, lower="+lowerBound+", upper="+upperBound, value >lowerBound && value < upperBound);
        }
    }


}