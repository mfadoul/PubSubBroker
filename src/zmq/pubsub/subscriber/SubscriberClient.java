package zmq.pubsub.subscriber;

import java.util.HashSet;
import java.util.Set;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public abstract class SubscriberClient {

	// Constructors
	public SubscriberClient(final String subscriberEndpoint) {
		this.subscriberEndpoint=subscriberEndpoint;

		ZMQ.Context context = ZMQ.context (1);
		subscriberSocket = context.socket(ZMQ.SUB);
		subscriberSocket.connect(subscriberEndpoint);
		
		// Still need so subscribe before beginning to listen to the socket.
	}

	public SubscriberClient() {
		this("tcp://127.0.0.1:6001");
	}

	// Public methods
	public String getSubscriberEndpoint() {
		return this.subscriberEndpoint;
	}
	
	public void subscribe(final int messageId) {
		messageIds.add(messageId);
	}
	
	public void subscribe(final Set<Integer> messageIds) {
		this.messageIds.addAll(messageIds);
	}

	public void unsubscribe(final int messageId) {
		this.messageIds.remove(messageId);
	}
	
	public void unsubscribeAll() {
		this.messageIds.clear();
	}
	
	public boolean isSubscribed(int messageId) {
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
	
	// Contents
	private final String subscriberEndpoint;
	private final Set<Integer> messageIds=new HashSet<Integer>();
	private final Socket subscriberSocket;
	
}
