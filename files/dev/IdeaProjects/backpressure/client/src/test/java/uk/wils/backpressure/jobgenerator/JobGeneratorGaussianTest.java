package uk.wils.backpressure.jobgenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        List<Integer> stdDevDurationList = new ArrayList<>();
        List<Integer> stdDevRetentionList = new ArrayList<>();
        double iterations = 10000.0;
        for (int i = 0; i<iterations ; i++) {
            Job job = jobGenerator.generate();
            int stdDevDuration = calaculateStdDevNumber(job.getDurationMillis(), durationMillis, duration1StdDevMillis);
            ensureSize(stdDevDurationList, stdDevDuration);
            stdDevDurationList.set(stdDevDuration-1, stdDevDurationList.get(stdDevDuration-1)+1);
            int stdDevRetention = calaculateStdDevNumber(job.getRetentionTimeMillis(), retentionPeriodMillis, retentionPeriod1StdDevMillis);
            ensureSize(stdDevRetentionList, stdDevRetention);
            stdDevRetentionList.set(stdDevRetention-1, stdDevRetentionList.get(stdDevRetention-1)+1);
        }
        double[] stdFractions = new double[] {0.682689492137086, 0.954499736103642, 0.997300203936740, 0.999936657516334, 0.999999426696856, 0.999999998026825, 0.999999999997440};
        double[] stdIntervals = new double[stdFractions.length];
        stdIntervals[0] = stdFractions[0];
        for (int i = 1 ; i < stdFractions.length; i++) {
            stdIntervals[i] = stdFractions[i] - stdFractions[i-1];
        }
        double tolerance = 0.3;
        for (int i = 0; i < stdDevDurationList.size(); i++) {
            double fraction = stdDevDurationList.get(i)/iterations;
            double lowerBound = stdIntervals[i] * (1-tolerance);
            double upperBound = stdIntervals[i] * (1+tolerance);
            assertTrue("StdDev "+(i+1)+" invalid, lower="+lowerBound+", upper="+upperBound+", value="+fraction, fraction >lowerBound && fraction < upperBound);
        }
    }

    @Test
    public void testEnsureSize() {
        List<Integer> list = new ArrayList<>();
        assertEquals("should be empty array", 0, list.size());
        ensureSize(list, 7);
        assertEquals("should be size 7", 7, list.size());
        ensureSize(list, 34);
        assertEquals("should be size 34", 34, list.size());
        ensureSize(list, 5);
        assertEquals("should be size 34", 34, list.size());

    }

    public void ensureSize(List<Integer> list, int newSize) {
        while (list.size() < newSize) {
            list.add(0);
        }
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