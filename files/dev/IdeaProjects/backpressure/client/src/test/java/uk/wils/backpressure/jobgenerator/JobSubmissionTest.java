package uk.wils.backpressure.jobgenerator;

import org.junit.Test;
import uk.wils.backpressure.util.GaussianRangedValueBuilder;
import uk.wils.backpressure.util.RangedValueBuilder;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by William O'Hara on 08/04/17.
 */
public class JobSubmissionTest {

    @Test
    public void getJob() throws Exception {
        RangedValueBuilder rangedValueBuilder = new GaussianRangedValueBuilder(50, 60);
        JobGeneratorGaussian jobGeneratorGaussian = new JobGeneratorGaussian(rangedValueBuilder, rangedValueBuilder);
        long delayMillies = 60;
        long activateTime = System.currentTimeMillis()+delayMillies;
        Job job = jobGeneratorGaussian.generate();
        JobSubmission jobSubmission = new JobSubmission(activateTime, job);
        Job job1 = jobSubmission.getJob();
        long candidateTime = jobSubmission.getDelay(TimeUnit.MILLISECONDS);
        long subtraction = delayMillies - candidateTime;
        assertTrue("delay subtraction: "+ subtraction, Math.abs(subtraction) < 2);
        assertEquals("job", job, job1);
    }

    @Test
    public void compareTo() throws Exception {
        RangedValueBuilder rangedValueBuilder = new GaussianRangedValueBuilder(50, 60);
        JobGeneratorGaussian jobGeneratorGaussian = new JobGeneratorGaussian(rangedValueBuilder, rangedValueBuilder);
        Job job = jobGeneratorGaussian.generate();
        long activateTime = 100000+System.currentTimeMillis();
        JobSubmission jobSubmissionReference = new JobSubmission(activateTime, job);
        JobSubmission jobSubmission1 = new JobSubmission(activateTime+10, job);
        JobSubmission jobSubmission2 = new JobSubmission(activateTime-10, job);
        JobSubmission jobSubmission3 = new JobSubmission(activateTime, job);
        assertTrue("delay comparision 1", jobSubmissionReference.compareTo(jobSubmission1)<0);
        assertTrue("delay comparision 2", jobSubmissionReference.compareTo(jobSubmission2)>0);
        assertTrue("delay comparision 3", jobSubmissionReference.compareTo(jobSubmission3)==0);
    }

}