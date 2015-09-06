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
		final Integer[] expectedSubscriptionsIdsArray = {3, 4, 5, 6, 1002, 1003, 2001, 2002};
		// Note: In the file SubscriberConfig.json, the messageNames "fireWeapon" and "updateTime" are invalid.
		final String[] expectedSubscriptionsNamesArray = {"setWind", "requestWind", "updateWind", "updateOcean", "setSoundSpeedProfile", "deleteEntity", "updateEntity"};
		
		expectedSubscriptionIds.addAll(Arrays.asList(expectedSubscriptionsIdsArray));
		expectedSubscriptionNames.addAll(Arrays.asList(expectedSubscriptionsNamesArray));
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
		assertEquals(expectedSubscriberEndpoint, subscriberClientJson.getSubscriberEndpoint());
		assertEquals(expectedSubscriptionIds, subscriberClientJson.getSubscriptions());
		
		for (String messageName: expectedSubscriptionNames) {
			assertTrue(subscriberClientJson.isSubscribed(messageName));
		}
		
		// Two messageNames that are listed in the JSON file, but do not have corresponding messageIds.
		assertFalse(subscriberClientJson.isSubscribed("fireWeapon"));
		assertFalse(subscriberClientJson.isSubscribed("updateTime"));
	}

	/**
	 * Test method for {@link zmq.pubsub.subscriber.SubscriberClientJson#SubscriberClientJson(zmq.pubsub.subscriber.SubscriberData)}.
	 */
	@Test
	public final void testSubscriberClientJsonSubscriberData() {
		SubscriberData subscriberData = new SubscriberData.Builder().jsonInputFilename(subscriberConfigFilename).build();
		SubscriberClientJson subscriberClientJson = new SubscriberClientJsonSimple(subscriberData);
		System.out.println("Subscriber Config = " + subscriberClientJson);
		assertEquals(expectedSubscriberEndpoint, subscriberClientJson.getSubscriberEndpoint());

		for (String messageName: expectedSubscriptionNames) {
			assertTrue(subscriberClientJson.isSubscribed(messageName));
		}
		
		// Two messageNames that are listed in the JSON file, but do not have corresponding messageIds.
		assertFalse(subscriberClientJson.isSubscribed("fireWeapon"));
		assertFalse(subscriberClientJson.isSubscribed("updateTime"));
	}

	private final String subscriberConfigFilename = "data/SubscriberConfig.json";
	private final String expectedSubscriberEndpoint = "tcp://127.0.0.1:6001";
	// Not exposed:
	// private final String expectedMessageMapFilename = "data/MessageMap.json";
	private final Set<Integer> expectedSubscriptionIds = new HashSet<Integer>();
	private final Set<String> expectedSubscriptionNames = new HashSet<String>();
}
