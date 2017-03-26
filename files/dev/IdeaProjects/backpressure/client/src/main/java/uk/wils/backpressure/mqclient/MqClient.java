package uk.wils.backpressure.mqclient;


import javax.jms.MapMessage;

/**
 * Created by William O'Hara on 05/03/17.
 */
public interface MqClient {
    void sendMessage(MqMessage mqMessage) throws MqClientException;

    void initialiseConnection() throws MqClientException;

    void setBrokerUrlString(String brokerUrlString);

    void createSession() throws MqClientException;

    void createQueue(String queueName) throws MqClientException;

    void createProducer(String queueName) throws MqClientException;

    MapMessage convertMessageToJms(MqMessage mqMessage) throws MqClientException;

    void close() throws MqClientException;
}
