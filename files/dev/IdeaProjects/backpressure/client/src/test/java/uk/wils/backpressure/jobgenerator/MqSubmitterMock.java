package uk.wils.backpressure.jobgenerator;

import uk.wils.backpressure.mqclient.MqClient;

import java.util.concurrent.DelayQueue;

/**
 * Created by William O'Hara on 08/04/17.
 */
public class MqSubmitterMock implements MqSubmitter{


    private DelayQueue<JobSubmission> submissionQueue;

    public int delayMillisBeforeQueueDrain = 100;
    JobSubmission lastJobSubmissionTaken = null;

    @Override
    public void beginSubmission() {
        try {
            Thread.sleep(delayMillisBeforeQueueDrain);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while ((lastJobSubmissionTaken = submissionQueue.poll()) != null) {}
    }

    public void drainQueue() {
        while (submissionQueue.size()>0) {
            try {
                lastJobSubmissionTaken = submissionQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setSubmissionQueue(DelayQueue<JobSubmission> submissionQueue) {

        this.submissionQueue = submissionQueue;
    }

    public DelayQueue<JobSubmission> getSubmissionQueue() {
        return submissionQueue;
    }
}
