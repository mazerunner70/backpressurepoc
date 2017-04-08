package uk.wils.backpressure.mqclient;

import javax.jms.MapMessage;

/**
 * Created by William O'Hara on 08/04/17.
 */
public class MqClientMock implements MqClient {
    private int sentMessagesCount = 0;
    private MqMessage lastMqMessage = null;

    public int getSentMessagesCount() {
        return sentMessagesCount;
    }

    public MqMessage getLastMqMessage() {
        return lastMqMessage;
    }

    @Override
    public void sendMessage(MqMessage mqMessage) throws MqClientException {
        sentMessagesCount++;
        lastMqMessage = mqMessage;
    }

    @Override
    public void initialiseConnection() throws MqClientException {

    }

    @Override
    public void setBrokerUrlString(String brokerUrlString) {

    }

    @Override
    public void createSession() throws MqClientException {

    }

    @Override
    public void createQueue(String queueName) throws MqClientException {

    }

    @Override
    public void createProducer(String queueName) throws MqClientException {

    }

    @Override
    public MapMessage convertMessageToJms(MqMessage mqMessage) throws MqClientException {
        return null;
    }

    @Override
    public void close() throws MqClientException {

    }
}
