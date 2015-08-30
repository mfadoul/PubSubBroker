package zmq.pubsub.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import zmq.pubsub.configuration.PubSubBrokerConfigurationJsonTest;
import zmq.pubsub.configuration.PubSubBrokerConfigurationXmlTest;
import zmq.pubsub.message.MessageMapJsonTest;
import zmq.pubsub.message.MessageMapTest;
import zmq.pubsub.subscriber.SubscriberClientTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	MessageMapTest.class, 
	MessageMapJsonTest.class,
	PubSubBrokerConfigurationJsonTest.class, 
	PubSubBrokerConfigurationXmlTest.class, 
	SubscriberClientTest.class })
public class PubSubBrokerConfigurationTests {

}
