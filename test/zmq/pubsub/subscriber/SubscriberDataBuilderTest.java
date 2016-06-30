package zmq.pubsub.subscriber;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
		SubscriberData.Builder builder = new Builder();
		assertNotNull(builder);
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

	/**
	 * Note: This tests both the jsonInputFilename and jsonInputStream builders
	 */
	@Test
	public final void testJsonInputFilename() {
		SubscriberData.Builder builder = new Builder();
		builder.jsonInputFilename("data/SubscriberConfig.json");
		subscriberData = builder.build();

		// Among other things...
		assertEquals("tcp://127.0.0.1:6001", subscriberData.getSubscriberEndpoint());
		assertTrue(subscriberData.getMessageMap().hasMessageName("deleteEntity"));
		assertTrue(subscriberData.getMessageMap().hasMessageName("updateEntity"));
		assertTrue(subscriberData.getMessageMap().hasMessageName("setWind"));
		assertTrue(subscriberData.getMessageMap().hasMessageId(4));
		assertTrue(subscriberData.getMessageMap().hasMessageId(5));
		assertTrue(subscriberData.getMessageMap().hasMessageId(1002));
		assertTrue(subscriberData.getMessageMap().hasMessageId(1003));

		// Even though these are in the JSON file, they don't actually exist, so they will fail.
		assertFalse(subscriberData.getMessageMap().hasMessageName("fireWeapon"));
		assertFalse(subscriberData.getMessageMap().hasMessageName("updateTime"));
		assertFalse(subscriberData.getMessageMap().hasMessageId(6));
		
		// Also assert that non-existent messages aren't being returned as valid.
		assertFalse(subscriberData.getMessageMap().hasMessageName("fakeMessage"));
		assertFalse(subscriberData.getMessageMap().hasMessageName(null));
		assertFalse(subscriberData.getMessageMap().hasMessageName(""));
		assertFalse(subscriberData.getMessageMap().hasMessageId(-1000));
		assertFalse(subscriberData.getMessageMap().hasMessageId(9999999));

	}

	@Test
	public final void testNonexistentJsonInputFilename() {
		final String nonexistentFilename = "data/NonexistentSubscriberConfig.json";
		
		// Redirect standard out.
		PrintStream originalStdErr = System.err;

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PrintStream printStream = new PrintStream(baos);
	    
	    // Tell Java to use your special stream
	    System.setErr(printStream);
	 

		SubscriberData.Builder builder = new Builder();
		builder.jsonInputFilename(nonexistentFilename);
		subscriberData = builder.build();

		// Put things back
	    System.out.flush();
	    System.setErr(originalStdErr);
	    
	    assertTrue(baos.toString().contains(
	    		"java.io.FileNotFoundException: " + nonexistentFilename + " (No such file or directory)"
	    		));	    
	}

	private SubscriberData subscriberData;
}
