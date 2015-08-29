package zmq.pubsub.subscriber;

import org.zeromq.ZMQ.Socket;

import zmq.pubsub.MessageUtils;

public class SubscriberClientSimple extends SubscriberClient {

	public SubscriberClientSimple(String subscriberEndpoint) {
		super(subscriberEndpoint);
	}

	public SubscriberClientSimple() {
	}

	@Override
	protected boolean receiveMessage(Socket subscriberSocket) {
		int messageId = MessageUtils.byteArrayToInt(subscriberSocket.recv());
		String messageContents = subscriberSocket.recvStr();
		
		System.out.println("Receive (ID=" + messageId + ").  Contents=" + messageContents);
		return true;
	}

}
