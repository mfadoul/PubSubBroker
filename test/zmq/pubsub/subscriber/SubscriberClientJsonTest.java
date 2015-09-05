/**
 * 
 */
package zmq.pubsub.subscriber;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author matt
 *
 */
public class SubscriberClientJsonTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Note: In the file SubscriberConfig.json, the messageNames "fireWeapon" and "updateTime" are invalid.
		final Integer[] expectedSubscriptionsArray = {3, 4, 5, 6, 1002, 1003, 2001, 2002};
		expectedSubscriptions.addAll(Arrays.asList(expectedSubscriptionsArray));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link zmq.pubsub.subscriber.SubscriberClientJson#SubscriberClientJson(java.lang.String)}.
	 */
	@Test
	public final void testSubscriberClientJsonString() {
				
		SubscriberClientJson subscriberClientJson = new SubscriberClientJsonSimple(subscriberConfigFilename);
		
		System.out.println("Subscriber Config = " + subscriberClientJson);
		assertEquals("tcp://127.0.0.1:6001", subscriberClientJson.getSubscriberEndpoint());
		assertEquals(expectedSubscriptions, subscriberClientJson.getSubscriptions());
		
	}

	/**
	 * Test method for {@link zmq.pubsub.subscriber.SubscriberClientJson#SubscriberClientJson(zmq.pubsub.subscriber.SubscriberData)}.
	 */
	@Test
	public final void testSubscriberClientJsonSubscriberData() {
		SubscriberData subscriberData = new SubscriberData.Builder().jsonInputFilename(subscriberConfigFilename).build();
		SubscriberClientJson subscriberClientJson = new SubscriberClientJsonSimple(subscriberData);
		System.out.println("Subscriber Config = " + subscriberClientJson);
		assertEquals("tcp://127.0.0.1:6001", subscriberClientJson.getSubscriberEndpoint());
	}

	private final String subscriberConfigFilename = "data/SubscriberConfig.json";
	private final String expectedSubscriberEndpoint = "tcp://127.0.0.1:6001";
	private final String expectedMessageMapFilename = "data/MessageMap.json";
	private final Set<Integer> expectedSubscriptions = new HashSet<Integer>();
	

}
