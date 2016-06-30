package zmq.pubsub.simpletest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import zmq.pubsub.message.MessageMap;
import zmq.pubsub.message.MessageMapJson;

/**
 *
 */
public final class SimpleMessageMapJsonTest {

	/**
	 * @param args No command line arguments are used
	 */
	public static void main(String[] args) {
		File initialFile = new File("data/MessageMap.json");
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(initialFile);
			MessageMap messageMap = new MessageMapJson(inputStream);
			System.out.println("pubSubBroker object = " + messageMap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
