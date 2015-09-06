package zmq.pubsub.subscriber;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import zmq.pubsub.message.MessageMap;

public class SubscriberDataTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Populated the expected messages in the map
		expectedMap.put(0,  "setWeather");
		expectedMap.put(1,  "requestWeather");
		expectedMap.put(2,  "updateWeather");
		expectedMap.put(3,  "setWind");
		expectedMap.put(4,  "requestWind");
		expectedMap.put(5,  "updateWind");
		expectedMap.put(1000,  "setOcean");
		expectedMap.put(1001,  "requestOcean");
		expectedMap.put(1002,  "updateOcean");
		expectedMap.put(1003,  "setSoundSpeedProfile");
		expectedMap.put(1004,  "requestSoundSpeedProfile");
		expectedMap.put(1005,  "updateSoundSpeedProfile");
		expectedMap.put(2000,  "createEntity");
		expectedMap.put(2001,  "deleteEntity");
		expectedMap.put(2002,  "updateEntity");

		final Integer[] expectedSubscriptionsIdsArray = {3, 4, 5, 6, 1002, 1003, 2001, 2002};
		expectedSubscriptionIds.addAll(Arrays.asList(expectedSubscriptionsIdsArray));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		subscriberData=new SubscriberData.Builder().jsonInputFilename(subscriberConfigFilename).build();
		
	}

	@After
	public void tearDown() throws Exception {
		subscriberData=null;
	}

	@Test
	public final void testGetSubscriberEndpoint() {
		assertEquals(expectedSubscriberEndpoint, subscriberData.getSubscriberEndpoint());
	}

	@Test
	public final void testGetMessageIds() {
		assertEquals(expectedSubscriptionIds, subscriberData.getMessageIds());
	}

	@Test
	public final void testGetMessageMap() {
		MessageMap messageMap = subscriberData.getMessageMap();
		assertNotNull(messageMap);
				
        for (Map.Entry<Integer, String> entry: expectedMap.entrySet()) {
        	try {
				assertEquals(entry.getValue(), messageMap.getMessageName(entry.getKey()));
				assertEquals(entry.getKey().intValue(), messageMap.getMessageId(entry.getValue()));
			} catch (IOException e) {
				e.printStackTrace();
				fail("Should not have a mismatch of the ID and name.");
			}
        }
	}

	SubscriberData subscriberData;
	
	private final String subscriberConfigFilename = "data/SubscriberConfig.json";
	private final String expectedSubscriberEndpoint = "tcp://127.0.0.1:6001";
	// Not exposed:
	private static final Set<Integer> expectedSubscriptionIds = new HashSet<Integer>();
	
	private static final Map<Integer, String> expectedMap = new HashMap<Integer, String>();
}
