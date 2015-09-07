package zmq.pubsub.subscriber;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zeromq.ZMQ;

import zmq.pubsub.message.MessageMap;

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
		
		MessageMap messageMap = subscriberClientSimple.getMessageMap();
		messageMap.addMessage(-1, null);
		messageMap.addMessage(0, "");
		messageMap.addMessage(1, "Message1");
		messageMap.addMessage(2, "Message2");
		messageMap.addMessage(3, "Message3");
		messageMap.addMessage(4, "Message4");
		
		// This is for subscribing by name.
		sampleMessageMap.put(-1, null);
		sampleMessageMap.put(0, "");
		sampleMessageMap.put(1, "Message1");
		sampleMessageMap.put(2, "Message2");


	}

	@After
	public void tearDown() throws Exception {
		subscriberClientSimple = null;
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
	public final void testSubscribeByNameString() {
		for (String messageName: sampleMessageMap.values()) {
			assertFalse(subscriberClientSimple.isSubscribed(messageName));
		}
		
		// Now, subscribe by name
		for (String messageName: sampleMessageMap.values()) {
			subscriberClientSimple.subscribeByName(messageName);
			assertTrue(subscriberClientSimple.isSubscribed(messageName));
		}
	}

	@Test
	public final void testSubscribeByNameSetOfString() {
		Set<String> messageNames = new HashSet<String>();
		
		messageNames.addAll(sampleMessageMap.values());

		for (String messageName: sampleMessageMap.values()) {
			assertFalse(subscriberClientSimple.isSubscribed(messageName));
		}

		// Now, subscribe by name
		subscriberClientSimple.subscribeByName(messageNames);
		
		for (String messageName: sampleMessageMap.values()) {
			assertTrue(subscriberClientSimple.isSubscribed(messageName));
		}
	}

	@Test
	public final void testUnsubscribeByName() {
		
		Set<String> messageNames = new HashSet<String>();
		
		messageNames.addAll(sampleMessageMap.values());

		// Now, subscribe by name
		assertEquals(messageNames.size(), subscriberClientSimple.subscribeByName(messageNames));
		
		for (String messageName: sampleMessageMap.values()) {
			assertTrue(subscriberClientSimple.isSubscribed(messageName));
		}
		
		// Unsubscribe one-by-one
		for (String messageName: sampleMessageMap.values()) {
			assertTrue(subscriberClientSimple.unsubscribeByName(messageName));
			assertFalse(subscriberClientSimple.isSubscribed(messageName));
		}
		
		// Try some bogus names
		assertFalse(subscriberClientSimple.unsubscribeByName("Message"));
		assertFalse(subscriberClientSimple.unsubscribeByName("MessageId1_invalid"));
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
	
	private final Map<Integer, String> sampleMessageMap = new HashMap<Integer, String>();

}
