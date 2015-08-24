package zmq.pubsub.simpletest;

import java.util.Date;
import java.util.Random;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import zmq.pubsub.MessageUtils;

public class SimplePublisherTest {

	public static int messageCount = 0;
	public static Random random = new Random(System.nanoTime());
	
	public static String zmqEndpointIpc = "ipc:///tmp/smmPublisherEndpoint";
	public static String zmqEndpointTcp = "tcp://127.0.0.1:6000";
	
	// Choose an endpoint here
	public static String zmqEndpoint = zmqEndpointIpc;


	public SimplePublisherTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		System.out.println("This is a simple publisher test");
		
		ZMQ.Context context = ZMQ.context (1);

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

	
	public static boolean sendMessage(Socket socket) {
		
		int messageId = Math.abs(random.nextInt()) % 10;
		messageCount++;
		
		//String messageHeader = Integer.toString(messageId);
		
		Date date = new java.util.Date();
		
		String messageContents = Integer.toString(messageCount) + ", Time = " + date.toString() + ", Published to [" + zmqEndpoint + "]";
		
		//socket.send(messageHeader, ZMQ.SNDMORE);
		socket.send(MessageUtils.intToByteArray(messageId), ZMQ.SNDMORE);
		socket.send(messageContents, 0);
		
		System.out.println("Send (ID=" + messageId + ").  Contents=" + messageContents);
		return true;
	}
}
