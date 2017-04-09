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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MqMessage mqMessage = (MqMessage) o;

        if (!headers.equals(mqMessage.headers)) return false;
        return body.equals(mqMessage.body);
    }

    @Override
    public int hashCode() {
        int result = headers.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }
}
