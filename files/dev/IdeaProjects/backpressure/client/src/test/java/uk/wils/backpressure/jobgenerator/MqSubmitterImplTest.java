package uk.wils.backpressure.jobgenerator;

import org.junit.Before;
import org.junit.Test;
import uk.wils.backpressure.mqclient.MqClient;
import uk.wils.backpressure.mqclient.MqClientException;
import uk.wils.backpressure.mqclient.MqClientMock;
import uk.wils.backpressure.mqclient.MqMessage;

import javax.jms.MapMessage;
import java.util.concurrent.DelayQueue;

import static org.junit.Assert.*;

/**
 * Created by William O'Hara on 08/04/17.
 */
public class MqSubmitterImplTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testDelayQueueConsumer() {
        Job job = new Job("id 1", 1, 2);
        JobSubmission jobSubmission = new JobSubmission(0, job);
        JobSubmission jobSubmission2 = new JobSubmission(1, null);
        DelayQueue<JobSubmission> jobSubmissions = new DelayQueue<>();
        jobSubmissions.add(jobSubmission);
        jobSubmissions.add(jobSubmission2);
        MqClientMock mqClientMock = new MqClientMock();
        MqSubmitterImpl.MqSubmit mqSubmit = new MqSubmitterImpl.MqSubmit();
        mqSubmit.setMqClient(mqClientMock);
        MqSubmitterImpl.DelayQueueConsumer delayQueueConsumer = new MqSubmitterImpl.DelayQueueConsumer();
        delayQueueConsumer.setSubmissionQueue(jobSubmissions);
        delayQueueConsumer.setMqSubmit(mqSubmit);
        delayQueueConsumer.run();
        assertEquals("queue should be empty", 0, jobSubmissions.size());
    }

    @Test
    public void testMqSubmitMarshall() throws MqClientException {
        MqClientMock mqClientMock = new MqClientMock();
        MqSubmitterImpl.MqSubmit mqSubmit = new MqSubmitterImpl.MqSubmit();
        mqSubmit.setMqClient(mqClientMock);
        Job job = new Job("id 1", 1, 2);
        MqMessage mqMessage = mqSubmit.marshall(job);
        assertEquals("three properties expected", 3, mqMessage.getBody().size());
        assertEquals("id", "id 1", mqMessage.getBody().get("id"));
        assertEquals("durationMillis", "1", mqMessage.getBody().get("durationMillis"));
        assertEquals("retentionTimeMillis", "2", mqMessage.getBody().get("retentionTimeMillis"));
        mqSubmit.execute(job);
        assertEquals("one message sent", 1, mqClientMock.getSentMessagesCount());
        assertSame("same message", mqMessage, mqClientMock.getLastMqMessage());
    }

}