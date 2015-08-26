package zmq.pubsub.configuration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PubSubBrokerConfigurationJsonTest.class, PubSubBrokerConfigurationXmlTest.class })
public class PubSubBrokerConfigurationTests {

}
