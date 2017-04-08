package uk.wils.backpressure.jobgenerator;

import org.junit.Test;
import uk.wils.backpressure.util.RangedValueBuilder;

import static org.junit.Assert.*;

/**
 * Created by William O'Hara on 08/04/17.
 */
public class JobSubmissionControlTest {

    @Test
    public void testConstrutor() {
        JobSubmissionControl jobSubmissionControl = new JobSubmissionControl(1, 2, 3, 4, 5, 6);
        RangedValueBuilder intervalRangedValueBuilder = jobSubmissionControl.getIntervalGenerator();
        assertEquals("interval average", 1, intervalRangedValueBuilder.getAverage());
        assertEquals("interval std dev", 2, intervalRangedValueBuilder.getRange68Percent());
        JobGenerator jobGenerator = jobSubmissionControl.getJobGenerator();
        RangedValueBuilder durationRangedValueBuilder = jobGenerator.getRangedValueBuilderDuration();
        assertEquals("duration average", 3, durationRangedValueBuilder.getAverage());
        assertEquals("duration std dev", 4, durationRangedValueBuilder.getRange68Percent());

        RangedValueBuilder retentionRangedValueBuilder = jobGenerator.getRangedValueBuilderRetention();
        assertEquals("retention average", 5, retentionRangedValueBuilder.getAverage());
        assertEquals("retention std dev", 6, retentionRangedValueBuilder.getRange68Percent());
    }

    @Test
    public void testaddJobsToSubmissionQueue() {
        JobSubmissionControl jobSubmissionControl = new JobSubmissionControl(2, 2, 3, 4, 5, 6);
        assertEquals("submission queue empty", 0, jobSubmissionControl.getSubmissionQueue().size());
        jobSubmissionControl.topUpSubmissionQueue(100);
        assertEquals("submission queue empty", 50, jobSubmissionControl.getSubmissionQueue().size());
        jobSubmissionControl.topUpSubmissionQueue(100);
        assertEquals("submission queue empty", 50, jobSubmissionControl.getSubmissionQueue().size());
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (jobSubmissionControl.getSubmissionQueue().poll() != null) {
        }
        assertTrue("Queue size must drop", jobSubmissionControl.getSubmissionQueue().size() < 50);
        jobSubmissionControl.topUpSubmissionQueue(100);
        assertEquals("submission queue empty", 50, jobSubmissionControl.getSubmissionQueue().size());
    }

    @Test
    public void testSubmitJobs1() {
        JobSubmissionControl jobSubmissionControl = new JobSubmissionControl(1, 2, 3, 4, 5, 6);
        int expectedDurationMillis = 500;
        MqSubmitterMock mqSubmitterMock = new MqSubmitterMock();
        long startTime = System.currentTimeMillis();
        jobSubmissionControl.submitJobs(expectedDurationMillis, mqSubmitterMock);
        long endTime = System.currentTimeMillis();
        long actualDuration = endTime - startTime;
        assertTrue("submission timing is off (actual:" + actualDuration + ", versus expected:" + expectedDurationMillis + ")",
                Math.abs((1.0 * actualDuration / expectedDurationMillis) - 1) < 0.1);
        assertSame("Delay queue reference passed over", jobSubmissionControl.getSubmissionQueue(), mqSubmitterMock.getSubmissionQueue());
        mqSubmitterMock.drainQueue();
        JobSubmission lastJobSubmission = mqSubmitterMock.lastJobSubmissionTaken;
        assertNull("Job must be null", lastJobSubmission.getJob());
    }

}