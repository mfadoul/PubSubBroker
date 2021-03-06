package zmq.pubsub.subscriber;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import zmq.pubsub.MessageUtils;
import zmq.pubsub.message.MessageMap;

public abstract class SubscriberClient implements SubscriberClientInterface {

	// Constructors
	public SubscriberClient() {
		this("tcp://127.0.0.1:6001");
	}

	public SubscriberClient(final String subscriberEndpoint) {
		this(subscriberEndpoint, new MessageMap());
		
		// The user may want to populate the message map.
	}

	public MessageMap getMessageMap() {
		return messageMap;
	}

	public SubscriberClient(String subscriberEndpoint, MessageMap messageMap) {
		this.subscriberEndpoint=subscriberEndpoint;

		ZMQ.Context context = ZMQ.context (1);
		subscriberSocket = context.socket(ZMQ.SUB);
		subscriberSocket.connect(subscriberEndpoint);
		this.messageMap = messageMap;		

		// Still need to subscribe before beginning to listen to the socket.
	}
	
	public SubscriberClient(String subscriberEndpoint, MessageMap messageMap, Set<Integer> messageIds) {
		this(subscriberEndpoint, messageMap);
		
		// Note: These subscriptions have to happen after the subscriberSocket is created.
		this.subscribe(messageIds);
	}

	// Public methods
	@Override
	public String getSubscriberEndpoint() {
		return this.subscriberEndpoint;
	}
	
	@Override
	public void subscribe(final int messageId) {
		messageIds.add(messageId);
		subscriberSocket.subscribe(MessageUtils.intToByteArray(messageId));
	}
	
	@Override
	public void subscribe(final Set<Integer> messageIds) {
		this.messageIds.addAll(messageIds);
		for (Integer messageId: messageIds) {
			subscriberSocket.subscribe(MessageUtils.intToByteArray(messageId));
		}
	}

	@Override
	public void unsubscribe(final int messageId) {
		this.messageIds.remove(messageId);
		subscriberSocket.unsubscribe(MessageUtils.intToByteArray(messageId));
	}
	
	@Override
	public void unsubscribeAll() {
		for (Integer messageId: this.getSubscriptions()) {
			subscriberSocket.unsubscribe(MessageUtils.intToByteArray(messageId));
		}
		this.messageIds.clear();
	}
	
	@Override
	public boolean subscribeByName(String messageName) {
		if (this.messageMap != null) {
			// Try to find a match
			try {
				Integer messageId = messageMap.getMessageId(messageName);
				if (messageId != null) {
					this.subscribe(messageId);
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public int subscribeByName(Set<String> messageNames) {
		int count = 0;
		for (String messageName: messageNames) {
			if (this.subscribeByName(messageName)) {
				++count;
			}
		}
		return count;
	}

	@Override
	public boolean unsubscribeByName(String messageName) {
		if (this.messageMap != null) {
			// Try to find a match
			try {
				Integer messageId = messageMap.getMessageId(messageName);
				if (messageId != null) {
					this.unsubscribe(messageId);
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean isSubscribed(final int messageId) {
		if (this.messageIds.contains(messageId))
			return true;
		else
			return false;
	}

	@Override
	public boolean isSubscribed(final String messageName) {
		if (this.messageMap != null) {
			// Try to find a match
			try {
				Integer messageId = messageMap.getMessageId(messageName);
				if (messageId != null) {
					if (this.messageIds.contains(messageId)) {
						return true;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public Set<Integer> getSubscriptions() {
		return this.messageIds;
	}
	
	@Override
	public final boolean subscriberLoop() {
		while (true) {
			receiveMessage(this.subscriberSocket);
		}
	}
	
	/**
	 * A method for child classes to process an incoming message.
	 * This is called in {@link #subscriberLoop()}
	 * @param subscriberSocket
	 * @return 
	 */
	protected abstract boolean receiveMessage(Socket subscriberSocket);
	
	// Designated endpoint for subscriptions (e.g. "tcp://127.0.0.1:6001")
	protected String subscriberEndpoint;
	
	// The purpose of this set of messageIds is to provide access to the list
	// of subscriptions, since the subscriptions are not visible when querying 
	// the ZeroMQ Socket object.
	private final Set<Integer> messageIds=new HashSet<Integer>();
	
	protected final Socket subscriberSocket;
	
	// This is a map of messageIds to messageNames
	protected final MessageMap messageMap;
	
}
