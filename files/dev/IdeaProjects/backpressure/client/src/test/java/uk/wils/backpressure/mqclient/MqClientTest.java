package uk.wils.backpressure.mqclient;

import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Rule;
import org.junit.Test;
import uk.wils.backpressure.util.ExceptionHelper;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.io.IOException;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static uk.wils.backpressure.mqclient.MqClientImpl.countPropertiesInAMessage;

/**
 * Created by William O'Hara on 05/03/17.
 */
public class MqClientTest {

    MqClientImpl mqClientImpl = null;
    MqClient mqClient = null;

    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    @org.junit.Before
    public void setUp() throws Exception {
        mqClientImpl = new MqClientImpl();
        mqClient = mqClientImpl;
        mqClient.setBrokerUrlString("vm://localhost");
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @Test
    public void testValidInitialiseConnection() throws MqClientException {
        mqClient.setBrokerUrlString("vm://localhost");
        mqClient.initialiseConnection();
        mqClient.close();
        mqClient.close();//close twice should not cause a problem
    }

    @Test
    public void testInvalidInitialiseConnection() throws MqClientException {
        mqClient.setBrokerUrlString("vt://localhost");
        try {
            mqClient.initialiseConnection();
            fail("Should have failed with JMS exception, as transport stream is invalid");
        } catch (MqClientException e) {
            //All good
        }
        //Close should not fail, should do nothing
        mqClient.close();
    }

    @Test
    public void testValidSession() throws MqClientException {
        mqClient.setBrokerUrlString("vm://localhost");
        mqClient.initialiseConnection();
        mqClient.createSession();
        mqClient.close();
        try {
            mqClient.createQueue("fails");
            fail("Should have failed as the session is closed");
        } catch (MqClientException e) {
            String exceptionText = ExceptionHelper.getExceptionText(e);
            if (!exceptionText.contains("The Session is closed")) {
                fail("Exception did not say session was closed\n"+ e.toString());
            }
        }
    }

    @Test
    public void testQueueCreation() throws MqClientException {
        mqClient.setBrokerUrlString("vm://localhost");
        mqClient.initialiseConnection();
        mqClient.createSession();
        mqClient.createQueue("test1");
        mqClient.close();
    }

    @Test
    public void testProducer() throws MqClientException {
        mqClient.setBrokerUrlString("vm://localhost");
        mqClient.initialiseConnection();
        mqClient.createSession();
        mqClient.createQueue("test1");
        mqClient.createProducer("Test-Q");
        mqClient.close();
    }

    @Test
    public void testSendMessage() throws MqClientException, IOException {
        mqClient.setBrokerUrlString("vm://localhost");
        mqClient.initialiseConnection();
        mqClient.createSession();
        mqClient.createQueue("test1");
        mqClient.createProducer("Test-Q");
        MqMessage mqMessage = new MqMessage();
        mqMessage.setBodyProperty("text", "Hello World");
        mqClient.sendMessage(mqMessage);
        mqClient.close();
    }

    @Test
    public void testCreateMessage() throws IOException, MqClientException,JMSException {
        mqClient.setBrokerUrlString("vm://localhost");
        mqClient.initialiseConnection();
        mqClient.createSession();
        mqClient.createQueue("test1");
        mqClient.createProducer("Test-Q");
        MqMessage mqMessage = new MqMessage();
        MapMessage mapMessage = mqClient.convertMessageToJms(mqMessage);
        assertEquals("should be empty", 0, countPropertiesInAMessage(mapMessage));
        assertEquals(asMap(mapMessage), new HashMap<String, Object>());

        mqMessage.setBodyProperty("text", "Hello World");
        mapMessage = mqClient.convertMessageToJms(mqMessage);
        assertEquals("should be one entry", 1, countPropertiesInAMessage(mapMessage));

        mqMessage.setBodyProperty("text1", "Hello World");
        mapMessage = mqClient.convertMessageToJms(mqMessage);
        assertEquals("should be two entries", 2, countPropertiesInAMessage(mapMessage));
        mqClient.close();
    }

    public Map<String, Object> asMap(MapMessage mapMessage) {
        Map<String, Object> map = new HashMap<>();
        List<String> keyList = null;
        try {
            //noinspection unchecked,unchecked
            keyList = Collections.list(mapMessage.getMapNames());
            for (String key : keyList) map.put(key, mapMessage.getObject(key));
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return map;
    }


}