package zmq.pubsub.message;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessageMapTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		messageMap = new MessageMap();
	}

	@After
	public void tearDown() throws Exception {
		messageMap = null;
	}

	@Test
	public final void testMessageMap() {
		assertNotNull(messageMap);
		assertEquals("MessageMap [map={}]", messageMap.toString());
	}

	@Test
	public final void testAddMessage() {
		messageMap.addMessage(1000, "TestMessage0");
		assertEquals("MessageMap [map={1000=TestMessage0}]", messageMap.toString());
		
		messageMap.addMessage(1001, "TestMessage1");

		assertTrue(messageMap.toString().contains("1000=TestMessage0"));
		assertTrue(messageMap.toString().contains("1001=TestMessage1"));

		// Repeat messageId
		messageMap.addMessage(1001, "TestMessage2");
		assertTrue(messageMap.toString().contains("1000=TestMessage0"));
		assertTrue(messageMap.toString().contains("1001=TestMessage2"));
		assertFalse(messageMap.toString().contains("1001=TestMessage1"));

		assertEquals("MessageMap [map={1001=TestMessage2, 1000=TestMessage0}]", messageMap.toString());
		
		// Repeat messageName
		messageMap.addMessage(1002, "TestMessage2");
		
		assertTrue(messageMap.toString().contains("1000=TestMessage0"));
		assertTrue(messageMap.toString().contains("1002=TestMessage2"));
		assertFalse(messageMap.toString().contains("1001=TestMessage1"));
		assertFalse(messageMap.toString().contains("1001=TestMessage2"));
	}

	@Test
	public final void testRemoveMessage() {
		int messageId;
		final int messageIdMin = 1000;
		final int messageIdMax = 1100;
		
		
		// Add a bunch of messages
		for (messageId = messageIdMin; messageId < messageIdMax; ++messageId) {
			messageMap.addMessage(messageId, "TestMessage" + messageId);
		}
		
		// Remove the messages
		for (messageId = messageIdMin; messageId < messageIdMax; ++messageId) {
			assertTrue(messageMap.toString().contains(messageId+"=TestMessage"+messageId));
			messageMap.removeMessage(messageId);
			assertFalse(messageMap.toString().contains(messageId+"=TestMessage"+messageId));
		}
	}

	@Test
	public final void testGetMessageName() {
		int messageId;
		final int messageIdMin = 1000;
		final int messageIdMax = 1100;
		
		// Add a bunch of messages
		for (messageId = messageIdMin; messageId < messageIdMax; messageId+=2) {
			messageMap.addMessage(messageId, "TestMessage" + messageId);
		}

		// Add a bunch of messages
		for (messageId = messageIdMin; messageId < messageIdMax; ++messageId) {
			String messageName="TestMessage"+messageId;
			try {
				// This will throw an exception for odd messageIds
				assertEquals(messageName, messageMap.getMessageName(messageId));
				
				// Only for even numbers
				assertEquals(messageId%2, 0);
			} catch (IOException e) {
				// For odd numbers, we will hit this exception.
				assertEquals(messageId%2, 1);
			}
		}
	}

	@Test
	public final void testGetMessageId() {
		int messageId;
		final int messageIdMin = 1000;
		final int messageIdMax = 1100;
		
		// Add a bunch of messages
		for (messageId = messageIdMin; messageId < messageIdMax; messageId+=2) {
			messageMap.addMessage(messageId, "TestMessage" + messageId);
		}

		for (messageId = messageIdMin; messageId < messageIdMax; ++messageId) {
			String expectedMessageName="TestMessage"+messageId;
			try {
				// This will throw an exception for odd messageIds
				assertEquals(messageId, messageMap.getMessageId(expectedMessageName));

				// Only for even numbers
				assertEquals(messageId%2, 0);
			} catch (IOException e) {
				// For odd numbers, we will hit this exception.
				assertEquals(messageId%2, 1);
			}
		}
	}

	@Test
	public final void testHasMessageId() {
		int messageId;
		final int messageIdMin = 1000;
		final int messageIdMax = 1100;
		
		// Add a bunch of messages
		for (messageId = messageIdMin; messageId < messageIdMax; messageId+=2) {
			messageMap.addMessage(messageId, "TestMessage" + messageId);
		}

		for (messageId = messageIdMin; messageId < messageIdMax; ++messageId) {
			// True for even numbers, false for odd numbers
			assertEquals(messageId%2==0, messageMap.hasMessageId(messageId));
		}
	}

	@Test
	public final void testHasMessageName() {
		int messageId;
		final int messageIdMin = 1000;
		final int messageIdMax = 1100;
		
		// Add a bunch of messages
		for (messageId = messageIdMin; messageId < messageIdMax; messageId+=2) {
			messageMap.addMessage(messageId, "TestMessage" + messageId);
		}

		for (messageId = messageIdMin; messageId < messageIdMax; ++messageId) {
			// True for even numbers, false for odd numbers
			assertEquals(messageId%2==0, messageMap.hasMessageName("TestMessage" +messageId));
		}
	}

	@Test
	public final void testGetAllMessageIds() {
		int messageId;
		final int messageIdMin = 1000;
		final int messageIdMax = 1100;
		
		// Add a bunch of messages
		for (messageId = messageIdMin; messageId < messageIdMax; messageId+=2) {
			messageMap.addMessage(messageId, "TestMessage" + messageId);
		}

		Set<Integer> messageIdSet = messageMap.getAllMessageIds();
		
		for (messageId = messageIdMin; messageId < messageIdMax; ++messageId) {
			// True for even numbers, false for odd numbers
			assertEquals(messageId%2==0, messageIdSet.contains(messageId));
		}
	}

	@Test
	public final void testToString() {
		int messageId;
		final int messageIdMin = 1000;
		final int messageIdMax = 1100;
		
		// Add a bunch of messages
		for (messageId = messageIdMin; messageId < messageIdMax; messageId+=2) {
			messageMap.addMessage(messageId, "TestMessage" + messageId);
		}

		for (messageId = messageIdMin; messageId < messageIdMax; ++messageId) {
			String expectedMessageName="TestMessage"+messageId;
			// True for even numbers, false for odd numbers
			assertEquals(messageId%2==0, messageMap.toString().contains(messageId+"="+expectedMessageName));
		}
	}
	
	private MessageMap messageMap = null;
}
