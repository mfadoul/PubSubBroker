package zmq.pubsub.subscriber;

import java.util.HashSet;
import java.util.Set;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import zmq.pubsub.MessageUtils;
import zmq.pubsub.message.MessageMap;

public abstract class SubscriberClient {

	// Constructors
	public SubscriberClient() {
		this("tcp://127.0.0.1:6001");
	}

	public SubscriberClient(final String subscriberEndpoint) {
		this.subscriberEndpoint=subscriberEndpoint;

		ZMQ.Context context = ZMQ.context (1);
		subscriberSocket = context.socket(ZMQ.SUB);
		subscriberSocket.connect(subscriberEndpoint);
		
		// Still need so subscribe before beginning to listen to the socket.
	}

	public SubscriberClient(String subscriberEndpoint, MessageMap messageMap) {
		this(subscriberEndpoint);
		this.messageMap = messageMap;		
	}
	
	public SubscriberClient(String subscriberEndpoint, MessageMap messageMap, Set<Integer> messageIds) {
		this(subscriberEndpoint, messageMap);
		
		// Note: These subscriptions have to happen after the subscriberSocket is created.
		this.subscribe(messageIds);
	}

	// Public methods
	public String getSubscriberEndpoint() {
		return this.subscriberEndpoint;
	}
	
	public void subscribe(final int messageId) {
		messageIds.add(messageId);
		subscriberSocket.subscribe(MessageUtils.intToByteArray(messageId));
	}
	
	public void subscribe(final Set<Integer> messageIds) {
		this.messageIds.addAll(messageIds);
		for (Integer messageId: messageIds) {
			subscriberSocket.subscribe(MessageUtils.intToByteArray(messageId));
		}
	}

	public void unsubscribe(final int messageId) {
		this.messageIds.remove(messageId);
		subscriberSocket.unsubscribe(MessageUtils.intToByteArray(messageId));
	}
	
	public void unsubscribeAll() {
		for (Integer messageId: this.getSubscriptions()) {
			subscriberSocket.unsubscribe(MessageUtils.intToByteArray(messageId));
		}
		this.messageIds.clear();
	}
	
	public boolean isSubscribed(final int messageId) {
		if (this.messageIds.contains(messageId))
			return true;
		else
			return false;
	}
	public Set<Integer> getSubscriptions() {
		return this.messageIds;
	}
	
	public final boolean subscriberLoop() {
		while (true) {
			receiveMessage(this.subscriberSocket);
		}
	}
	
	protected abstract boolean receiveMessage(Socket subscriberSocket);
	
	// Designated endpoint for subscriptions (e.g. "tcp://127.0.0.1:6001")
	protected String subscriberEndpoint;
	
	// The purpose of this set of messageIds is to provide access to the list
	// of subscriptions, since the subscriptions are not visible when querying 
	// the ZeroMQ Socket object.
	private final Set<Integer> messageIds=new HashSet<Integer>();
	
	protected final Socket subscriberSocket;
	
	// This is a map of messageIds to messageNames
	protected MessageMap messageMap = null;
	
}
