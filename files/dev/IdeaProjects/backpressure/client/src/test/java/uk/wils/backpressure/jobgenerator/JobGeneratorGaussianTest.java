package uk.wils.backpressure.jobgenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vagrant on 12/03/17.
 */
public class JobGeneratorGaussianTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGenerate() {
        JobGenerator jobGenerator = new JobGeneratorGaussian();
        int durationMillis = 43*60*1000;
        int duration1StdDevMillis= 5*60*1000;
        int retentionPeriodMillis = 20*1000;
        int retentionPeriod1StdDevMillis = 5*60*1000;
        jobGenerator.setDuration(durationMillis);
        jobGenerator.setDuration68PercentRange(duration1StdDevMillis);
        jobGenerator.setRetentionPeriod(retentionPeriodMillis);
        jobGenerator.setRetention68PercentRange(retentionPeriod1StdDevMillis);
//        for (int i = 0; i<1000 ; i++) {
//            Job job = jobGenerator.generate();
//
//            assertTrue();
//        }

    }

    @Test
    public void testCalaculateStdDevNumber() {
        assertEquals("Should be 1 StdDev", 1, calaculateStdDevNumber(10, 7, 4));
        assertEquals("Should be 1 StdDev", 1, calaculateStdDevNumber(5, 7, 4));
        assertEquals("Should be 2 StdDev", 2, calaculateStdDevNumber(14, 7, 4));
        assertEquals("Should be 2 StdDev", 2, calaculateStdDevNumber(0, 7, 4));
    }

    int calaculateStdDevNumber(int value, int midPoint, int stdDev) {
        return Math.abs((value-midPoint)/stdDev)+1;
    }

}