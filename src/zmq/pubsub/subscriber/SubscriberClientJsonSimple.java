package zmq.pubsub.subscriber;

import org.zeromq.ZMQ.Socket;

import zmq.pubsub.MessageUtils;

public class SubscriberClientJsonSimple extends SubscriberClientJson {

	public SubscriberClientJsonSimple(String subscriberConfigFilename) {
		super(subscriberConfigFilename);
	}
	
	public SubscriberClientJsonSimple(SubscriberData subscriberData) {
		super(subscriberData);
	}

	@Override
	protected boolean receiveMessage(Socket subscriberSocket) {
		int messageId = MessageUtils.byteArrayToInt(subscriberSocket.recv());
		String messageContents = subscriberSocket.recvStr();
		
		System.out.println("Receive (ID=" + messageId + ").  Contents=" + messageContents);
		return true;
	}

}
