package zmq.pubsub.subscriber;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import zmq.pubsub.message.MessageMap;
import zmq.pubsub.subscriber.SubscriberData.Builder;

public class SubscriberDataBuilderTest extends Builder {

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
		subscriberData=null;
	}

	@Test
	public final void testBuilder() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSubscriptionEndpoint() {
		String subscriptionEndpoint = "tcp://localhost:6001";
		SubscriberData.Builder builder = new Builder();
		builder.subscriptionEndpoint(subscriptionEndpoint);
		
		subscriberData = builder.build();
		assertEquals(subscriptionEndpoint, subscriberData.getSubscriberEndpoint());
	}

	@Test
	public final void testMessageIds() {
		Set<Integer> messageIds = new HashSet<Integer>();
		messageIds.add(1);
		messageIds.add(2);
		messageIds.add(3);
		messageIds.add(100);
		messageIds.add(101);
		
		SubscriberData.Builder builder = new Builder();
		builder.messageIds(messageIds);
		
		subscriberData = builder.build();
		assertEquals(messageIds, subscriberData.getMessageIds());
	}

	@Test
	public final void testMessageMap() {
		MessageMap messageMap = new MessageMap();
		messageMap.addMessage(-1, null);
		messageMap.addMessage(0, "");
		messageMap.addMessage(1, "Message1");
		messageMap.addMessage(2, "Message2");
		messageMap.addMessage(3, "Message3");
		
		SubscriberData.Builder builder = new Builder();
		builder.messageMap(messageMap);
		
		subscriberData = builder.build();

		for (int i = 0; i < 4; ++i) {
			assertTrue(subscriberData.getMessageMap().hasMessageId(i));
			try {
				assertTrue(subscriberData.getMessageMap().hasMessageName(messageMap.getMessageName(i)));
			} catch (IOException e) {
				fail("Should not have any mismatches.");
				e.printStackTrace();
			}
		}
		
	}

	@Test
	public final void testJsonInputFilename() {
		SubscriberData.Builder builder = new Builder();
		builder.jsonInputFilename("data/SubscriberConfig.json");
		subscriberData = builder.build();

		// Among other things...
		assertEquals("tcp://127.0.0.1:6001", subscriberData.getSubscriberEndpoint());
	}

	@Test
	public final void testJsonInputStream() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testBuild() {
		SubscriberData.Builder builder = new Builder();
	}

	private SubscriberData subscriberData;
}
