package uk.wils.backpressure.jobgenerator;

import java.util.concurrent.DelayQueue;

/**
 * Created by William O'Hara on 08/04/17.
 */
public interface MqSubmitter {

    void beginSubmission();

    void setSubmissionQueue(DelayQueue<JobSubmission> submissionQueue);

}
