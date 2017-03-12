package uk.wils.backpressure.mqclient;

import javax.jms.JMSException;
import java.io.IOException;

/**
 * Created by vagrant on 11/03/17.
 */
public class MqClientException extends IOException{


    public MqClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
