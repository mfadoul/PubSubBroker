package zmq.pubsub.subscriber;

import java.util.Set;

public interface SubscriberClientInterface {

	// Public methods
	String getSubscriberEndpoint();

	// Methods that use message IDs
	void subscribe(int messageId);
	void subscribe(Set<Integer> messageIds);
	void unsubscribe(int messageId);
	boolean isSubscribed(int messageId);
	Set<Integer> getSubscriptions();

	// Methods that use message names

	// @Return whether the messageName existed, resulting in a subscribe.
	// Use message names
	boolean subscribeByName(String messageName);
	
	// @return the number of messages successfully added as subscriptions
	// Throw IO exception if MessageMap is null
	int subscribeByName(Set<String> messageNames);
	
	// @Return whether the messageName existed, resulting in an unsubscribe.
	boolean unsubscribeByName(String messageName);

	boolean isSubscribed(String messageName);

	// Other messages
	void unsubscribeAll();


	boolean subscriberLoop();

}