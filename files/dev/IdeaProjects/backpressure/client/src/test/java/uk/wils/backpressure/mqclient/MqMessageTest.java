package uk.wils.backpressure.mqclient;

import org.junit.Test;

import javax.jms.Message;

import static org.junit.Assert.*;

/**
 * Created by vagrant on 09/03/17.
 */
public class MqMessageTest {

    @Test
    public void testAddBodyProperty() {
        MqMessage mqMessage = new MqMessage();
        assertEquals("mqMessage body not empty", 0, mqMessage.getBody().size());
        mqMessage.setBodyProperty("key", "value");
        assertEquals("mqMessage body should be size 1", 1, mqMessage.getBody().size());
    }

}