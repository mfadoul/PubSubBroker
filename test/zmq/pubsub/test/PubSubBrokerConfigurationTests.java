package zmq.pubsub.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import zmq.pubsub.BrokerTest;
import zmq.pubsub.configuration.PubSubBrokerConfigurationJsonTest;
import zmq.pubsub.configuration.PubSubBrokerConfigurationXmlTest;
import zmq.pubsub.message.MessageMapJsonTest;
import zmq.pubsub.message.MessageMapTest;
import zmq.pubsub.subscriber.SubscriberClientJsonTest;
import zmq.pubsub.subscriber.SubscriberClientTest;
import zmq.pubsub.subscriber.SubscriberDataTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	BrokerTest.class,
	MessageMapTest.class, 
	MessageMapJsonTest.class,
	PubSubBrokerConfigurationJsonTest.class, 
	PubSubBrokerConfigurationXmlTest.class, 
	SubscriberClientTest.class,
	SubscriberClientJsonTest.class,
	SubscriberDataTest.class})
public class PubSubBrokerConfigurationTests {

}
