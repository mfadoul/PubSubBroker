package zmq.pubsub.message;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessageMapJsonTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		File initialFile = new File("data/MessageMap.json");
		InputStream inputStream = new FileInputStream(initialFile);
		messageMap = new MessageMapJson(inputStream);
	}

	@After
	public void tearDown() throws Exception {
		messageMap = null;
	}

	@Test
	public final void testGetMessageName() {
		Set<Integer> expectedSet = expectedMap.keySet();

		try {
			for (Integer messageId : expectedSet) {
				assertEquals(expectedMap.get(messageId), messageMap.getMessageName(messageId));
			}
		} catch (IOException e) {
			fail("Should never get an exception when calling getMessageName() in this test.");
		}

		// Pass an invalid messageId;
		try {
			String name = messageMap.getMessageName(-1);
			fail("Should never get an exception when calling getMessageName() in this test. Name = " + name);
		} catch (IOException e) {
			// We should get here.
		}		
	}

	@Test
	public final void testGetMessageId() {
		Collection<String> expectedNameSet = expectedMap.values();
		for (String messageName: expectedNameSet) {
			Integer messageId;
			try {
				messageId = messageMap.getMessageId(messageName);
				assertEquals(expectedMap.get(messageId), messageName);
			} catch (IOException e) {
				fail("Shouldn't have a mismatch in this test.");
			}
		}
		
		try {
			// Try some bad messageId values.
			Integer temp = messageMap.getMessageId(null);
			System.out.println("Can't get here (" + temp + ")");
			fail("Shouldn't have a match in this test.");
		} catch (IOException e) {
			// Expected to get here
		}
	}

	@Test
	public final void testHasMessageId() {
		Set<Integer> expectedSet = expectedMap.keySet();

		for (Integer messageId: expectedSet) {
			assertTrue(messageMap.hasMessageId(messageId));
		}
		
		// Try some bad messageId values.
		assertFalse(messageMap.hasMessageId(-1000));
		assertFalse(messageMap.hasMessageId(100));
	}

	@Test
	public final void testHasMessageName() {
		Set<Integer> expectedSet = expectedMap.keySet();

		for (Integer messageId: expectedSet) {
			String expectedName= expectedMap.get(messageId);
			assertTrue(messageMap.hasMessageName(expectedName));
		}

		// Try some bad messageName values.
		assertFalse(messageMap.hasMessageName(null));
		assertFalse(messageMap.hasMessageName("InvalidMessageName"));
		assertFalse(messageMap.hasMessageName(""));
	}

	@Test
	public final void testGetAllMessageIds() {
		Set<Integer> allMessageIds = messageMap.getAllMessageIds();
		Set<Integer> expectedSet = expectedMap.keySet();
		assertEquals(expectedSet.toString(), allMessageIds.toString());
	}

	private MessageMap messageMap = null;
	
	private static final Map<Integer, String> expectedMap;
    static {
        Map<Integer, String> tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "setWeather");
        tempMap.put(1, "requestWeather");
        tempMap.put(2, "updateWeather");
        tempMap.put(3, "setWind");
        tempMap.put(4, "requestWind");
        tempMap.put(5, "updateWind");
        tempMap.put(1000, "setOcean");
        tempMap.put(1001, "requestOcean");
        tempMap.put(1002, "updateOcean");
        tempMap.put(1003, "setSoundSpeedProfile");
        tempMap.put(1004, "requestSoundSpeedProfile");
        tempMap.put(1005, "updateSoundSpeedProfile");
        tempMap.put(2000, "createEntity");
        tempMap.put(2001, "deleteEntity");
        tempMap.put(2002, "updateEntity");
        expectedMap = Collections.unmodifiableMap(tempMap);
    }

}
