package zmq.pubsub.subscriber;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zeromq.ZMQ;

public class SubscriberClientTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		subscriberClientSimple = new SubscriberClientSimple();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testSubscriberClient() {
		SubscriberClientInterface subscriberClient=null;
		
		subscriberClient = new SubscriberClientSimple();
		assertNotNull(subscriberClient);
	}

	@Test
	public final void testGetSubscriberEndpoint() {

		assertTrue(this.subscriberEndpointTcp.equals(subscriberClientSimple.getSubscriberEndpoint()));
	}

	@Test
	public final void testSubscribe() {
		final int messageId = 123;
		assertFalse(subscriberClientSimple.isSubscribed(messageId));
		subscriberClientSimple.subscribe(messageId);
		assertTrue(subscriberClientSimple.isSubscribed(messageId));
	}

	@Test
	public final void testSubscribeAll() {
		final Set<Integer> messageSet = new HashSet<Integer>();
		
		messageSet.add(101);
		messageSet.add(102);
		messageSet.add(103);
		
		for (Integer messageId: messageSet) {
			assertFalse(subscriberClientSimple.isSubscribed(messageId));
		}
		
		subscriberClientSimple.subscribe(messageSet);
		
		for (Integer messageId: messageSet) {
			assertTrue(subscriberClientSimple.isSubscribed(messageId));
		}
	}

	@Test
	public final void testUnsubscribe() {
		final int messageId = 123;
		subscriberClientSimple.subscribe(messageId);
		assertTrue(subscriberClientSimple.isSubscribed(messageId));
		subscriberClientSimple.unsubscribe(messageId);
		assertFalse(subscriberClientSimple.isSubscribed(messageId));

	}

	@Test
	public final void testUnsubscribeAll() {
		final Set<Integer> messageSet = new HashSet<Integer>();
		
		messageSet.add(101);
		messageSet.add(102);
		messageSet.add(103);
		
		
		subscriberClientSimple.subscribe(messageSet);
		for (Integer messageId: messageSet) {
			assertTrue(subscriberClientSimple.isSubscribed(messageId));
		}
		
		subscriberClientSimple.unsubscribeAll();
		
		for (Integer messageId: messageSet) {
			assertFalse(subscriberClientSimple.isSubscribed(messageId));
		}
		
		assertTrue(0==subscriberClientSimple.getSubscriptions().size());
	}

	@Test
	public final void testIsSubscribed() {
		final int messageId = 123;
		subscriberClientSimple.subscribe(messageId);
		assertTrue(subscriberClientSimple.isSubscribed(messageId));
		subscriberClientSimple.unsubscribe(messageId);
		assertFalse(subscriberClientSimple.isSubscribed(messageId));
	}

	@Test
	public final void testGetSubscriptions() {
		final int messageId1 = 101;
		final int messageId2 = 102;
		final int messageId3 = 103;
		
		assertEquals(0, subscriberClientSimple.getSubscriptions().size());

		subscriberClientSimple.subscribe(messageId1);
		assertEquals(1, subscriberClientSimple.getSubscriptions().size());
		assertTrue(subscriberClientSimple.getSubscriptions().contains(messageId1));
		assertFalse(subscriberClientSimple.getSubscriptions().contains(messageId2));
		assertFalse(subscriberClientSimple.getSubscriptions().contains(messageId3));
		
		subscriberClientSimple.subscribe(messageId2);
		assertEquals(2, subscriberClientSimple.getSubscriptions().size());
		assertTrue(subscriberClientSimple.getSubscriptions().contains(messageId1));
		assertTrue(subscriberClientSimple.getSubscriptions().contains(messageId2));
		assertFalse(subscriberClientSimple.getSubscriptions().contains(messageId3));

		subscriberClientSimple.subscribe(messageId3);
		assertEquals(3, subscriberClientSimple.getSubscriptions().size());
		assertTrue(subscriberClientSimple.getSubscriptions().contains(messageId1));
		assertTrue(subscriberClientSimple.getSubscriptions().contains(messageId2));
		assertTrue(subscriberClientSimple.getSubscriptions().contains(messageId3));

		// Make sure that inserting a duplicate doesn't change the set's size.
		subscriberClientSimple.subscribe(messageId3);
		assertEquals(3, subscriberClientSimple.getSubscriptions().size());

		subscriberClientSimple.unsubscribe(messageId1);
		assertEquals(2, subscriberClientSimple.getSubscriptions().size());
		assertFalse(subscriberClientSimple.getSubscriptions().contains(messageId1));
		assertTrue(subscriberClientSimple.getSubscriptions().contains(messageId2));
		assertTrue(subscriberClientSimple.getSubscriptions().contains(messageId3));

		subscriberClientSimple.unsubscribeAll();
		assertEquals(0, subscriberClientSimple.getSubscriptions().size());
		assertFalse(subscriberClientSimple.getSubscriptions().contains(messageId1));
		assertFalse(subscriberClientSimple.getSubscriptions().contains(messageId2));
		assertFalse(subscriberClientSimple.getSubscriptions().contains(messageId3));

	}

	@Test
	public final void testZmqVersion() {
		assertTrue(ZMQ.getMajorVersion()>=0);
		assertTrue(ZMQ.getMinorVersion()>=0);
		assertNotNull(ZMQ.getVersionString());
	}
	
	SubscriberClientSimple subscriberClientSimple;
	final String subscriberEndpointTcp="tcp://127.0.0.1:6001";
	final String subscriberEndpointIpc = "ipc:///tmp/smmSubscriberEndpoint";
}
