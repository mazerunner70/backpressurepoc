package uk.wils.backpressure;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import uk.wils.backpressure.mqclient.MqClientTest;
import uk.wils.backpressure.mqclient.MqMessageTest;
import uk.wils.backpressure.util.ExceptionHelperTest;

/**
 * Created by William O'Hara on 11/03/17.
 */
public class TestSuite {

    @RunWith(Suite.class)
    @Suite.SuiteClasses({
            MqClientTest.class,
            MqMessageTest.class,
            ExceptionHelperTest.class
    })

    public class FeatureTestSuite {
        // the class remains empty,
        // used only as a holder for the above annotations
    }
}
