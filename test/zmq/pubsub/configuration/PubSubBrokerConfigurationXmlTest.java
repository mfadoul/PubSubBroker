package zmq.pubsub.configuration;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PubSubBrokerConfigurationXmlTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		pubSubBrokerConfiguration = new PubSubBrokerConfigurationXml("data/PubSubBroker.xml");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetName() {
		assertEquals("Main Broker", pubSubBrokerConfiguration.getName());
	}

	@Test
	public final void testGetBrokerBindingsCount() {
		// There should be two external connection in the XML file.
		assertEquals(2, pubSubBrokerConfiguration.getBrokerBindingsCount());
	}

	@Test
	public final void testGetBrokerBinding() {
		for (int i=-100; i < 100; ++i) {
			switch (i) {
			case 0:
				assertEquals("localToMachine", pubSubBrokerConfiguration.getBrokerBinding(0).getName());
				assertEquals("ipc:///tmp/smmPublisherEndpoint", pubSubBrokerConfiguration.getBrokerBinding(0).getPublisherEndpoint());
				assertEquals("ipc:///tmp/smmSubscriberEndpoint", pubSubBrokerConfiguration.getBrokerBinding(0).getSubscriberEndpoint());
				break;
			case 1:
				assertEquals("tcpBased", pubSubBrokerConfiguration.getBrokerBinding(1).getName());
				assertEquals("tcp://*:6000", pubSubBrokerConfiguration.getBrokerBinding(1).getPublisherEndpoint());
				assertEquals("tcp://*:6001", pubSubBrokerConfiguration.getBrokerBinding(1).getSubscriberEndpoint());
				break;
			default:
				{
					boolean caughtException = false;
					try {
						assertNull(pubSubBrokerConfiguration.getBrokerBinding(i));
					} catch (IndexOutOfBoundsException e) {
						caughtException = true;
					}
					assertTrue(caughtException);
				}
			}
		}	
	}

	@Test
	public final void testGetBrokerExternalConnectionCount() {
		// There should be one external connection in the XML file.
		assertEquals(1, pubSubBrokerConfiguration.getBrokerExternalConnectionCount());
	}

	@Test
	public final void testGetBrokerExternalConnection() {
		for (int i=-100; i < 100; ++i) {
			switch (i) {
			case 0:
				assertEquals("existingBrokerConnection", pubSubBrokerConfiguration.getBrokerExternalConnection(0).getName());
				assertEquals("tcp://localhost:16000", pubSubBrokerConfiguration.getBrokerExternalConnection(0).getPublisherEndpoint());
				assertEquals("tcp://localhost:16001", pubSubBrokerConfiguration.getBrokerExternalConnection(0).getSubscriberEndpoint());
				break;
			default:
				{
					boolean caughtException = false;
					try {
						assertNull(pubSubBrokerConfiguration.getBrokerExternalConnection(i));
					} catch (IndexOutOfBoundsException e) {
						caughtException = true;
					}
					assertTrue(caughtException);
				}
			}
		}
	}

	@Test
	public final void testToString() {
		String expectedToString = "PubSubBrokerConfigurationXml [brokerName=Main Broker, brokerBindings=" +
	        "[BrokerConnection [name=localToMachine, publisherEndpoint=ipc:///tmp/smmPublisherEndpoint, " +
			"subscriberEndpoint=ipc:///tmp/smmSubscriberEndpoint], BrokerConnection [name=tcpBased, " +
	        "publisherEndpoint=tcp://*:6000, subscriberEndpoint=tcp://*:6001]], " +
			"brokerExternalConnections=[BrokerConnection [name=existingBrokerConnection, " +
	        "publisherEndpoint=tcp://localhost:16000, subscriberEndpoint=tcp://localhost:16001]]]";
		assertEquals(expectedToString, pubSubBrokerConfiguration.toString());
	}

	static PubSubBrokerConfigurationXml pubSubBrokerConfiguration;
}
