package zmq.pubsub;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import zmq.pubsub.configuration.PubSubBrokerConfiguration;

public class BrokerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	public final void testBrokerIntInt() {
		final int xPubPort = 7001;
		final int xSubPort = 7002;
		final String expectedName = "SimplePubSubBroker";
		final String expectedEndpointPairName ="SimpleEndpointPair";
		
		broker = new Broker(xPubPort, xSubPort);
		assertFalse(broker.isInitialized());
		PubSubBrokerConfiguration pubSubBrokerConfiguration = broker.getPubSubBrokerConfiguration();
		assertNotNull(pubSubBrokerConfiguration);
		assertEquals (expectedName, pubSubBrokerConfiguration.getName());
		assertEquals (expectedEndpointPairName, pubSubBrokerConfiguration.getBrokerBinding(0).getName());

		assertEquals (1, pubSubBrokerConfiguration.getBrokerBindingsCount());
		assertEquals ("tcp://*:"+xPubPort + "", pubSubBrokerConfiguration.getBrokerBinding(0).getPublisherEndpoint());
		assertEquals ("tcp://*:"+xSubPort + "", pubSubBrokerConfiguration.getBrokerBinding(0).getSubscriberEndpoint());
	}

	@Test
	public final void testBrokerString() {
		final String pubSubBrokerXmlFilename = "data/pubSubBroker.xml";
		final String pubSubBrokerXmlFilename_invalid = "data/pubSubBroker_invalid.xml";
		final String expectedName = "Main Broker (XML)";
		
		boolean caughtFileNoteFoundException = false;
		
		// First, try with a bogus filename
		try {
			broker = new Broker(pubSubBrokerXmlFilename_invalid);
			fail("The file " + pubSubBrokerXmlFilename_invalid + " should not exist.");	
		} catch (FileNotFoundException e) {
			caughtFileNoteFoundException = true;
		} catch (IOException e) {
			// Some other exception?
		}
		assertTrue(caughtFileNoteFoundException);
		assertNull(broker);

		// Re-initialize for next test.
		caughtFileNoteFoundException=false;

		try {
			broker = new Broker(pubSubBrokerXmlFilename);
		} catch (FileNotFoundException e) {
			fail("The file " + pubSubBrokerXmlFilename + " should exist.");
			caughtFileNoteFoundException = true;
		} catch (IOException e) {
		}

		assertFalse(caughtFileNoteFoundException);
		assertNotNull(broker);
		
		PubSubBrokerConfiguration pubSubBrokerConfiguration = broker.getPubSubBrokerConfiguration();
		assertNotNull(pubSubBrokerConfiguration);
		assertEquals (expectedName, pubSubBrokerConfiguration.getName());
		assertEquals (2, pubSubBrokerConfiguration.getBrokerBindingsCount());
		
		assertEquals ("localToMachine", pubSubBrokerConfiguration.getBrokerBinding(0).getName());
		assertEquals ("ipc:///tmp/smmPublisherEndpoint", pubSubBrokerConfiguration.getBrokerBinding(0).getPublisherEndpoint());
		assertEquals ("ipc:///tmp/smmSubscriberEndpoint", pubSubBrokerConfiguration.getBrokerBinding(0).getSubscriberEndpoint());

		assertEquals ("tcpBased", pubSubBrokerConfiguration.getBrokerBinding(1).getName());
		assertEquals ("tcp://*:6000", pubSubBrokerConfiguration.getBrokerBinding(1).getPublisherEndpoint());
		assertEquals ("tcp://*:6001", pubSubBrokerConfiguration.getBrokerBinding(1).getSubscriberEndpoint());
		
		assertEquals (1, pubSubBrokerConfiguration.getBrokerExternalConnectionCount());
		assertEquals ("existingBrokerConnection", pubSubBrokerConfiguration.getBrokerExternalConnection(0).getName());
		assertEquals ("tcp://localhost:16000", pubSubBrokerConfiguration.getBrokerExternalConnection(0).getPublisherEndpoint());
		assertEquals ("tcp://localhost:16001", pubSubBrokerConfiguration.getBrokerExternalConnection(0).getSubscriberEndpoint());

	}

	@Test
	public final void testBrokerInputStream() {
		final String pubSubBrokerJsonFilename = "data/pubSubBroker.json";
		final String expectedName = "Main Broker (JSON)";

		InputStream brokerJsonInputStream = null;
		
		try {
			File brokerJsonFile = new File(pubSubBrokerJsonFilename);
			brokerJsonInputStream = new FileInputStream(brokerJsonFile);
			assertNotNull(brokerJsonInputStream);
		} catch (FileNotFoundException e) {
			fail("The file " + pubSubBrokerJsonFilename + " should exist.");			
		}

		try {
			broker = new Broker(pubSubBrokerJsonFilename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(broker);
		
		PubSubBrokerConfiguration pubSubBrokerConfiguration = broker.getPubSubBrokerConfiguration();
		assertNotNull(pubSubBrokerConfiguration);
		assertEquals (expectedName, pubSubBrokerConfiguration.getName());
		assertEquals (2, pubSubBrokerConfiguration.getBrokerBindingsCount());
		
		assertEquals ("localToMachine", pubSubBrokerConfiguration.getBrokerBinding(0).getName());
		assertEquals ("ipc:///tmp/smmPublisherEndpoint", pubSubBrokerConfiguration.getBrokerBinding(0).getPublisherEndpoint());
		assertEquals ("ipc:///tmp/smmSubscriberEndpoint", pubSubBrokerConfiguration.getBrokerBinding(0).getSubscriberEndpoint());

		assertEquals ("tcpBased", pubSubBrokerConfiguration.getBrokerBinding(1).getName());
		assertEquals ("tcp://*:6000", pubSubBrokerConfiguration.getBrokerBinding(1).getPublisherEndpoint());
		assertEquals ("tcp://*:6001", pubSubBrokerConfiguration.getBrokerBinding(1).getSubscriberEndpoint());
		
		assertEquals (1, pubSubBrokerConfiguration.getBrokerExternalConnectionCount());
		assertEquals ("existingBrokerConnection", pubSubBrokerConfiguration.getBrokerExternalConnection(0).getName());
		assertEquals ("tcp://localhost:16000", pubSubBrokerConfiguration.getBrokerExternalConnection(0).getPublisherEndpoint());
		assertEquals ("tcp://localhost:16001", pubSubBrokerConfiguration.getBrokerExternalConnection(0).getSubscriberEndpoint());
	}

	@Test
	public final void testIsInitialized() {
		final int xPubPort = 7001;
		final int xSubPort = 7002;
		
		broker = new Broker(xPubPort, xSubPort);
		assertFalse(broker.isInitialized());
		
		// Spawn the broker as a thread.
		Thread brokerThread = new Thread(broker);
		brokerThread.start();
		
		//Sleep for 1/10 second, while the child thread (brokerThread) executes.
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(broker.isInitialized());

		// Kill the thread.
		brokerThread.interrupt();
		broker.freeResources();
		assertFalse(broker.isInitialized());		
	}

	@Test
	public final void testFreeResources() {
		final int xPubPort = 7001;
		final int xSubPort = 7002;
		
		broker = new Broker(xPubPort, xSubPort);
		
		// Spawn the broker as a thread.
		Thread brokerThread = new Thread(broker);
		brokerThread.start();
		
		//Sleep for 1/10 second, while the child thread (brokerThread) executes.
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(broker.isInitialized());

		// Kill the thread.
		brokerThread.interrupt();
		broker.freeResources();
		assertFalse(broker.isInitialized());
		System.out.println("Broker=" + broker);

		assertTrue(broker.toString().contains("xpubSocket=null, xsubSocket=null, initialized=false"));
		
		// Try freeResources again.  Nothing bad should happen.
		broker.freeResources();
		assertFalse(broker.isInitialized());
		assertTrue(broker.toString().contains("xpubSocket=null, xsubSocket=null, initialized=false"));
	}
	
	
	// TODO: Test for the main() method, using different arguments.
	/*
	@Test
	public final void testMainOneArg() {
		String [] args = {};
		
		//Broker.main(args);
		fail("Not yet implemented");
	}
    */
	
	Broker broker = null;
}
