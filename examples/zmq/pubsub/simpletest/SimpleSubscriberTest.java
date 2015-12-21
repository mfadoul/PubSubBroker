package zmq.pubsub.simpletest;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import zmq.pubsub.MessageUtils;

/**
 *
 */
public class SimpleSubscriberTest {

	public static String zmqEndpointTcp = "tcp://127.0.0.1:6001";
	public static String zmqEndpointIpc = "ipc:///tmp/smmSubscriberEndpoint";

	// Choose an endpoint here
	public static String zmqEndpoint = zmqEndpointIpc;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("This is a simple subscriber test");
		System.out.println("Listening on the following endpoint: " + zmqEndpoint);

		ZMQ.Context context = ZMQ.context (1);

		Socket subscriberSocket = context.socket(ZMQ.SUB);
		subscriberSocket.connect(zmqEndpoint);

		// Subscribe
		subscriberSocket.subscribe(MessageUtils.intToByteArray(0));
		subscriberSocket.subscribe(MessageUtils.intToByteArray(3));
		subscriberSocket.subscribe(MessageUtils.intToByteArray(6));
		subscriberSocket.subscribe(MessageUtils.intToByteArray(9));

		// The old way to register, using strings for keys
		//subscriberSocket.subscribe("6".getBytes());
		//subscriberSocket.subscribe("9".getBytes());
		System.out.println("Listening to messages from this subscription endpoint: " + zmqEndpoint);

		while (true) {
			receiveMessage(subscriberSocket);
		}
	}

	/**
	 * @param socket
	 * @return
	 */
	public static boolean receiveMessage(Socket socket) {
		int messageId = MessageUtils.byteArrayToInt(socket.recv());
		//String messageIdString = socket.recvStr();
		String messageContents = socket.recvStr();

		System.out.println("Receive (ID=" + messageId + ").  Contents=" + messageContents);

		return true;
	}
}
