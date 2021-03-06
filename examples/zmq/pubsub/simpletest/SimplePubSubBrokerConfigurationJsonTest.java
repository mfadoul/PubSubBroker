package zmq.pubsub.simpletest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import zmq.pubsub.configuration.PubSubBrokerConfigurationJson;

/**
 *
 */
public class SimplePubSubBrokerConfigurationJsonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File initialFile = new File("data/PubSubBroker.json");
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(initialFile);
			PubSubBrokerConfigurationJson pubSubBrokerConfigurationJson = new PubSubBrokerConfigurationJson(inputStream);
			System.out.println("pubSubBroker object = " + pubSubBrokerConfigurationJson);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
