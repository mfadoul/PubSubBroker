package zmq.pubsub.simpletest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;
import java.util.Set;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import zmq.pubsub.MessageUtils;
import zmq.pubsub.message.MessageMap;
import zmq.pubsub.message.MessageMapJson;

/**
 *
 */
public final class SimplePublisherTest {

	public static int messageCount = 0;
	public static Random random = new Random(System.nanoTime());

	public static String zmqEndpointIpc = "ipc:///tmp/smmPublisherEndpoint";
	public static String zmqEndpointTcp = "tcp://127.0.0.1:6000";

	// Choose an endpoint here
	public static String zmqEndpoint = zmqEndpointIpc;

	public static MessageMap messageMap = null;

	public static void main(String[] args) {
		System.out.println("This is a simple publisher test");

		ZMQ.Context context = ZMQ.context (1);
		try {
			File messageMapFile = new File("data/MessageMap.json");
			InputStream inputStream;
			inputStream = new FileInputStream(messageMapFile);
			messageMap = new MessageMapJson(inputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Socket publisherSocket = context.socket(ZMQ.PUB);
		publisherSocket.connect(zmqEndpoint);

		while (true) {
			sendMessage(publisherSocket);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param socket
	 * @return
	 */
	public static boolean sendMessage(Socket socket) {
		Set<Integer> messages = messageMap.getAllMessageIds();

		int messageIndex = Math.abs(random.nextInt()) % messages.size();
		int messageId = (int) messages.toArray()[messageIndex];
		messageCount++;

		Date date = new java.util.Date();

		String messageContents = Integer.toString(messageCount) + ", Time = " + date.toString() + ", Published to [" + zmqEndpoint + "]";

		socket.send(MessageUtils.intToByteArray(messageId), ZMQ.SNDMORE);
		socket.send(messageContents, 0);

		System.out.println("Send (ID=" + messageId + ").  Contents=" + messageContents);
		return true;
	}
}
