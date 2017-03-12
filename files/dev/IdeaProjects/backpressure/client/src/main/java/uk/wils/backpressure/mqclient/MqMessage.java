package uk.wils.backpressure.mqclient;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vagrant on 05/03/17.
 */
public class MqMessage {
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> body = new HashMap<String, String>();

    public void setBodyProperty(String key, String value) {
        body.put(key, value);
    }

    public Map<String,String> getBody() {
        return body;
    }


}
