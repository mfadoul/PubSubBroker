package zmq.pubsub.subscriber;

import org.zeromq.ZMQ.Socket;

import zmq.pubsub.MessageUtils;

/**
 * A simple subscriber that prints out the each incoming message
 *
 */
public class SubscriberClientSimple extends SubscriberClient {

	@Override
	protected boolean receiveMessage(Socket subscriberSocket) {
		int messageId = MessageUtils.byteArrayToInt(subscriberSocket.recv());
		String messageContents = subscriberSocket.recvStr();
		
		System.out.println("Receive (ID=" + messageId + ").  Contents=" + messageContents);
		return true;
	}

}
