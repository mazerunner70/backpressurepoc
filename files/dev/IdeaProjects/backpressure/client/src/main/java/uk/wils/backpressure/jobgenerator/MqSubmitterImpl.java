package uk.wils.backpressure.jobgenerator;

import uk.wils.backpressure.mqclient.MqClient;
import uk.wils.backpressure.mqclient.MqClientException;
import uk.wils.backpressure.mqclient.MqClientImpl;
import uk.wils.backpressure.mqclient.MqMessage;

import java.util.concurrent.DelayQueue;

/**
 * Created by William O'Hara on 08/04/17.
 */
public class MqSubmitterImpl implements MqSubmitter{

    private DelayQueueConsumer delayQueueConsumer;
    private MqSubmit mqSubmit;


    public MqSubmitterImpl() {
        mqSubmit = new MqSubmit();
        delayQueueConsumer = new DelayQueueConsumer();
        delayQueueConsumer.mqSubmit = mqSubmit;
    }

    @Override
    public void beginSubmission() {
        new Thread(delayQueueConsumer).start();
    }

    @Override
    public void setSubmissionQueue(DelayQueue<JobSubmission> submissionQueue) {
        delayQueueConsumer.submissionQueue = submissionQueue;
    }




    public static class DelayQueueConsumer implements Runnable{
        public void setSubmissionQueue(DelayQueue<JobSubmission> submissionQueue) {
            this.submissionQueue = submissionQueue;
        }

        public void setMqSubmit(MqSubmit mqSubmit) {
            this.mqSubmit = mqSubmit;
        }

        private DelayQueue<JobSubmission> submissionQueue;
        private MqSubmit mqSubmit = new MqSubmit();
        boolean isConsumerActive = false;

        @Override
        public void run() {
            isConsumerActive = true;
            while (isConsumerActive) {
                try {
                    JobSubmission jobSubmission = submissionQueue.take();
                    Job job = jobSubmission.getJob();
                    if (job == null) {
                        isConsumerActive = false;
                    } else {
                        mqSubmit.execute(job);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (MqClientException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static class MqSubmit {
        public void setMqClient(MqClient mqClient) {
            this.mqClient = mqClient;
        }

        private MqClient mqClient = new MqClientImpl();
        public void execute(Job job) throws MqClientException {
            MqMessage mqMessage = marshall(job);
            mqClient.sendMessage(mqMessage);

        }
        public MqMessage marshall(Job job) {
            MqMessage mqMessage = new MqMessage();
            mqMessage.setBodyProperty("id", job.getId());
            mqMessage.setBodyProperty("durationMillis", Integer.toString(job.getDurationMillis()));
            mqMessage.setBodyProperty("retentionTimeMillis", Integer.toString(job.getRetentionTimeMillis()));
            return mqMessage;
        }
    }

}
