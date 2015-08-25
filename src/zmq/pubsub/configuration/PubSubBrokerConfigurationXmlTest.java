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
		pubSubBrokerConfigurationXml = new PubSubBrokerConfigurationXml("data/PubSubBroker.xml");
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
	public final void testPubSubBrokerConfigurationXml() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetName() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetBrokerBindingsCount() {
		// There should be two external connection in the XML file.
		assertEquals(2, pubSubBrokerConfigurationXml.getBrokerBindingsCount());
	}

	@Test
	public final void testGetBrokerBinding() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetBrokerExternalConnectionCount() {
		// There should be one external connection in the XML file.
		assertEquals(1, pubSubBrokerConfigurationXml.getBrokerExternalConnectionCount());
	}

	@Test
	public final void testGetBrokerExternalConnection() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	static PubSubBrokerConfigurationXml pubSubBrokerConfigurationXml;
}
