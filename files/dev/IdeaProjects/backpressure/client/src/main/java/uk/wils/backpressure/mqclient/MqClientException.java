package uk.wils.backpressure.mqclient;

import java.io.IOException;

/**
 * Created by William O'Hara on 11/03/17.
 */
public class MqClientException extends IOException{


    public MqClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
