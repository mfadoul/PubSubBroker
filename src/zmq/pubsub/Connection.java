package zmq.pubsub;

import java.util.HashSet;
import java.util.Set;

public class Connection {

	public Connection(String name) {
		this.name=name;
	}

	// Public methods
	
	public String getName() {
		return this.name;
	}
	
	public void subscribe(int messageId) {
		messageIds.add(messageId);
	}
	
	public void subscribeAll(final Set<Integer> messageIds) {
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
	
	// Contents
	private String name;
	private final Set<Integer> messageIds=new HashSet<Integer>();
}
