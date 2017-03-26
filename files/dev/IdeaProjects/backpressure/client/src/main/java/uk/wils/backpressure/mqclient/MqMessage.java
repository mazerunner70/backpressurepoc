package uk.wils.backpressure.mqclient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by William O'Hara on 05/03/17.
 */
public class MqMessage {
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> body = new HashMap<>();

    public void setBodyProperty(String key, String value) {
        body.put(key, value);
    }

    public Map<String,String> getBody() {
        return body;
    }


}
