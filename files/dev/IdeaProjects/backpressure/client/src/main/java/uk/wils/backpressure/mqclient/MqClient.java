package uk.wils.backpressure.mqclient;


import javax.jms.MapMessage;
import java.io.IOException;

/**
 * Created by vagrant on 05/03/17.
 */
public interface MqClient {
    void sendMessage(MqMessage mqMessage) throws IOException;

    void initialiseConnection() throws MqClientException;

    void setBrokerUrlString(String brokerUrlString);

    void createSession() throws MqClientException;

    void createQueue(String queueName) throws MqClientException;

    void createProducer(String s) throws MqClientException;

    MapMessage convertMessageToJms(MqMessage mqMessage) throws IOException;

    void close() throws MqClientException;
}
