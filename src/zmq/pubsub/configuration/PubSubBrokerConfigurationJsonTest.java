package zmq.pubsub.configuration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PubSubBrokerConfigurationJsonTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File initialFile = new File("data/PubSubBroker.json");
		InputStream inputStream = new FileInputStream(initialFile);
		pubSubBrokerConfigurationJson = new PubSubBrokerConfigurationJson(inputStream);
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
	public final void testPubSubBrokerConfigurationJson() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetName() {
		assertTrue("Main Broker".equals(pubSubBrokerConfigurationJson.getName()));
	}

	@Test
	public final void testGetBrokerBindingsCount() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetBrokerBinding() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetBrokerConnectionCount() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetBrokerConnection() {
		fail("Not yet implemented"); // TODO
	}

	static PubSubBrokerConfigurationJson pubSubBrokerConfigurationJson;
}
