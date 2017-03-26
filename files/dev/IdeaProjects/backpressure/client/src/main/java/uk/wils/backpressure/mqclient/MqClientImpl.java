package uk.wils.backpressure.mqclient;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by William O'Hara on 05/03/17.
 */
public class MqClientImpl implements MqClient {

    private String brokerUrlString;
    private Connection connection = null;
    private Session session = null;
    private final Logger logger = LoggerFactory.getLogger(MqClientImpl.class);
    private MessageProducer messageProducer;

    public void initialiseConnection() throws MqClientException {
        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrlString);
        // Create a Connection
        try {
            connection = connectionFactory.createConnection();
            connection.start();
        } catch (JMSException e) {
            throw new MqClientException("Failed to create connection", e);
        }
        logger.info("Created connection to broker at {}", brokerUrlString);
    }

    public void setBrokerUrlString(String brokerUrlString) {
        this.brokerUrlString = brokerUrlString;
    }

    public void createSession() throws MqClientException {
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            throw new MqClientException("Failed to create session", e);
        }
        logger.info("Created session to broker at {}", brokerUrlString);
    }

    public void createQueue(String queueName) throws MqClientException {
        try {
            session.createQueue(queueName);
        } catch (JMSException e) {
            throw new MqClientException("Failed to create queue", e);
        }
        logger.info("Created queue name {} to broker at {}",queueName, brokerUrlString);
    }

    public void createProducer(String queueName) throws MqClientException {
        Destination destination = null;
        try {
            destination = session.createQueue(queueName);
            messageProducer = session.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (JMSException e) {
            throw new MqClientException("Failed to create producer", e);
        }
        logger.info("Created producer for queue name {} to broker at {}",queueName, brokerUrlString);
    }

    public void sendMessage(MqMessage mqMessage) throws MqClientException{
        MapMessage mapMessage = convertMessageToJms(mqMessage);
        try {
            messageProducer.send(mapMessage);
        } catch (JMSException e) {
            throw new MqClientException("Failed to create producer", e);
        }
        logger.info("Sent message via producer for name {} to broker at {}", messageProducer.toString(), brokerUrlString);
    }

    public MapMessage convertMessageToJms(MqMessage mqMessage) throws MqClientException {
        MapMessage mapMessage = null;
        try {
            mapMessage = session.createMapMessage();
            mqMessage.getBody().forEach(rethrowBiConsumer(mapMessage::setString));
//            for (String key : mqMessage.getBody().keySet()) {
//                logger.debug("Storing key: {}, value: {} into mapMessage", key, mqMessage.getBody().get(key));
//                mapMessage.setString(key, mqMessage.getBody().get(key));
//            }
            logger.info("Storing {} properties in a MapMessage", countPropertiesInAMessage(mapMessage));
        } catch (JMSException e) {
            throw new MqClientException("Could not create map mqMessage", e);
        }
        return mapMessage;
    }

    public static long countPropertiesInAMessage(MapMessage mapMessage) throws JMSException{
        //noinspection unchecked
        return enumerationAsStream(mapMessage.getMapNames()).count();
    }


    @Override
    public void close() throws MqClientException {
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                throw new MqClientException("Could not close connection", e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                throw new MqClientException("Could not close connection", e);
            }
        }
    }

    public static <T> Stream<T> enumerationAsStream(Enumeration<T> e) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<T>() {
                            public T next() {
                                return e.nextElement();
                            }
                            public boolean hasNext() {
                                return e.hasMoreElements();
                            }
                        },
                        Spliterator.ORDERED), false);
    }



    @FunctionalInterface
    public interface BiConsumer_WithExceptions<T, U, E extends Exception> {
        void accept(T t, U u) throws E;
    }

    public static <T, U, E extends Exception> BiConsumer<T, U> rethrowBiConsumer(BiConsumer_WithExceptions<T, U, E> biConsumer) {
        return (t, u) -> {
            try { biConsumer.accept(t, u); }
            catch (Exception exception) { throwAsUnchecked(exception); }
        };
    }

    @SuppressWarnings ("unchecked")
    private static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E { throw (E)exception; }


}
