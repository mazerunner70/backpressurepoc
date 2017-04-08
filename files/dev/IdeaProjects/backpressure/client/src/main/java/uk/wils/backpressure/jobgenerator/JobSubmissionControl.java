package uk.wils.backpressure.jobgenerator;

import uk.wils.backpressure.util.GaussianRangedValueBuilder;
import uk.wils.backpressure.util.RangedValueBuilder;

import java.util.Queue;
import java.util.concurrent.DelayQueue;

/**
 * Created by William O'Hara on 15/03/17.
 */
public class JobSubmissionControl {

    private DelayQueue<JobSubmission> submissionQueue = new DelayQueue<>();

    private RangedValueBuilder intervalGenerator;
    private JobGenerator jobGenerator;

    public JobSubmissionControl(int intervalAverage, int intervalrange68Percent,
                                int durationAverage, int durationRange68Percent,
                                int retentionAverage, int retentionRange68Percent) {
        intervalGenerator = new GaussianRangedValueBuilder(intervalAverage, intervalrange68Percent);
        RangedValueBuilder jobDurationRangedValueBuilder = new GaussianRangedValueBuilder(durationAverage, durationRange68Percent);
        RangedValueBuilder jobRetentionRangedValueBuilder = new GaussianRangedValueBuilder(retentionAverage, retentionRange68Percent);
        jobGenerator = new JobGeneratorGaussian(jobDurationRangedValueBuilder, jobRetentionRangedValueBuilder);
    }

    public void topUpSubmissionQueue(int totalDuration) {
        long currentTime = System.currentTimeMillis();
        long offsetTime = intervalGenerator.getAverage()*submissionQueue.size();
        while (offsetTime < totalDuration){
            offsetTime += intervalGenerator.generate();
            JobSubmission jobSubmission = new JobSubmission(currentTime+offsetTime, jobGenerator.generate());
            submissionQueue.add(jobSubmission);
        }
    }

    public void submitJobs(int durationMillis, MqSubmitter mqSubmitter) {
        mqSubmitter.setSubmissionQueue(submissionQueue);
        long endTime = System.currentTimeMillis() + durationMillis;
        mqSubmitter.beginSubmission();
        while (endTime > System.currentTimeMillis()) {
            topUpSubmissionQueue(durationMillis);
            try {
                Thread.sleep(durationMillis / 10 + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                //ignore
            }
        }
        submissionQueue.clear();
        submissionQueue.add(new JobSubmission(0, null)); // End of stream identifier
    }


    public RangedValueBuilder getIntervalGenerator() {
        return intervalGenerator;
    }

    public JobGenerator getJobGenerator() {
        return jobGenerator;
    }

    public Queue<JobSubmission> getSubmissionQueue() {
        return submissionQueue;
    }

}
