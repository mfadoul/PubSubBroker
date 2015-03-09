package zmq.pubsub.simpletest;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import zmq.pubsub.MessageUtils;

public class SimpleSubscriberTest {

	public SimpleSubscriberTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String zmqEndpoint = "tcp://127.0.0.1:5555";
		
		System.out.println("This is a simple subscriber test");
		
		ZMQ.Context context = ZMQ.context (1);

		Socket subscriberSocket = context.socket(ZMQ.SUB);
		subscriberSocket.connect(zmqEndpoint);

		// Subscribe
		//subscriberSocket.subscribe("0".getBytes());
		subscriberSocket.subscribe(MessageUtils.intToByteArray(3));
		//subscriberSocket.subscribe("6".getBytes());
		//subscriberSocket.subscribe("9".getBytes());
	
		while (true) {
			receiveMessage(subscriberSocket);
		}
	}
	public static boolean receiveMessage(Socket socket) {
		int messageId = MessageUtils.byteArrayToInt(socket.recv());
		//String messageIdString = socket.recvStr();
		String messageContents = socket.recvStr();
		
		System.out.println("Receive (ID=" + messageId + ").  Contents=" + messageContents);

		return true;
	}

}
