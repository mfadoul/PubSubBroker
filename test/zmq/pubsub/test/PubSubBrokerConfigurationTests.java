package zmq.pubsub.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import zmq.pubsub.configuration.PubSubBrokerConfigurationJsonTest;
import zmq.pubsub.configuration.PubSubBrokerConfigurationXmlTest;
import zmq.pubsub.SubscriberClientTest;

@RunWith(Suite.class)
@SuiteClasses({ PubSubBrokerConfigurationJsonTest.class, PubSubBrokerConfigurationXmlTest.class, SubscriberClientTest.class })
public class PubSubBrokerConfigurationTests {

}
