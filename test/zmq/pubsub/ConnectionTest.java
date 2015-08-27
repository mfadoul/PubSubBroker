package zmq.pubsub;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zeromq.ZMQ;

public class ConnectionTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		connection = new Connection(this.connectionName);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testConnection() {
		Connection connection2=null;
		
		connection2 = new Connection("Temp");
		assertNotNull(connection2);
	}

	@Test
	public final void testGetName() {

		assertTrue(this.connectionName.equals(connection.getName()));
	}

	@Test
	public final void testSubscribe() {
		final int messageId = 123;
		assertFalse(connection.isSubscribed(messageId));
		connection.subscribe(messageId);
		assertTrue(connection.isSubscribed(messageId));
	}

	@Test
	public final void testSubscribeAll() {
		final Set<Integer> messageSet = new HashSet<Integer>();
		
		messageSet.add(101);
		messageSet.add(102);
		messageSet.add(103);
		
		for (Integer messageId: messageSet) {
			assertFalse(connection.isSubscribed(messageId));
		}
		
		connection.subscribeAll(messageSet);
		
		for (Integer messageId: messageSet) {
			assertTrue(connection.isSubscribed(messageId));
		}
	}

	@Test
	public final void testUnsubscribe() {
		final int messageId = 123;
		connection.subscribe(messageId);
		assertTrue(connection.isSubscribed(messageId));
		connection.unsubscribe(messageId);
		assertFalse(connection.isSubscribed(messageId));

	}

	@Test
	public final void testUnsubscribeAll() {
		final Set<Integer> messageSet = new HashSet<Integer>();
		
		messageSet.add(101);
		messageSet.add(102);
		messageSet.add(103);
		
		
		connection.subscribeAll(messageSet);
		for (Integer messageId: messageSet) {
			assertTrue(connection.isSubscribed(messageId));
		}
		
		connection.unsubscribeAll();
		
		for (Integer messageId: messageSet) {
			assertFalse(connection.isSubscribed(messageId));
		}
		
		assertTrue(0==connection.getSubscriptions().size());
	}

	@Test
	public final void testIsSubscribed() {
		final int messageId = 123;
		connection.subscribe(messageId);
		assertTrue(connection.isSubscribed(messageId));
		connection.unsubscribe(messageId);
		assertFalse(connection.isSubscribed(messageId));
	}

	@Test
	public final void testGetSubscriptions() {
		final int messageId1 = 101;
		final int messageId2 = 102;
		final int messageId3 = 103;
		
		assertEquals(0, connection.getSubscriptions().size());

		connection.subscribe(messageId1);
		assertEquals(1, connection.getSubscriptions().size());
		assertTrue(connection.getSubscriptions().contains(messageId1));
		assertFalse(connection.getSubscriptions().contains(messageId2));
		assertFalse(connection.getSubscriptions().contains(messageId3));
		
		connection.subscribe(messageId2);
		assertEquals(2, connection.getSubscriptions().size());
		assertTrue(connection.getSubscriptions().contains(messageId1));
		assertTrue(connection.getSubscriptions().contains(messageId2));
		assertFalse(connection.getSubscriptions().contains(messageId3));

		connection.subscribe(messageId3);
		assertEquals(3, connection.getSubscriptions().size());
		assertTrue(connection.getSubscriptions().contains(messageId1));
		assertTrue(connection.getSubscriptions().contains(messageId2));
		assertTrue(connection.getSubscriptions().contains(messageId3));

		// Make sure that inserting a duplicate doesn't change the set's size.
		connection.subscribe(messageId3);
		assertEquals(3, connection.getSubscriptions().size());

		connection.unsubscribe(messageId1);
		assertEquals(2, connection.getSubscriptions().size());
		assertFalse(connection.getSubscriptions().contains(messageId1));
		assertTrue(connection.getSubscriptions().contains(messageId2));
		assertTrue(connection.getSubscriptions().contains(messageId3));

		connection.unsubscribeAll();
		assertEquals(0, connection.getSubscriptions().size());
		assertFalse(connection.getSubscriptions().contains(messageId1));
		assertFalse(connection.getSubscriptions().contains(messageId2));
		assertFalse(connection.getSubscriptions().contains(messageId3));

	}

	@Test
	public final void testZmqVersion() {
		assertTrue(ZMQ.getMajorVersion()>=0);
		assertTrue(ZMQ.getMinorVersion()>=0);
		assertNotNull(ZMQ.getVersionString());
	}
	
	Connection connection;
	final String connectionName="TestConnection";
}
