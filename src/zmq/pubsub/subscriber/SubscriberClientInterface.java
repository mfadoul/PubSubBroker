package zmq.pubsub.subscriber;

import java.util.Set;

public interface SubscriberClientInterface {
	/**
	 * @return the name of the subscriber endpoint
	 */
	String getSubscriberEndpoint();

	// Methods that use message IDs
	
	/**
	 * Subscribe to a single message
	 * @param messageId The message ID that the client will subscribe to.
	 */
	void subscribe(int messageId);
	
	/**
	 * Subscribe to a set of messages
	 * @param messageIds the set of message IDs that the client will subscribe to.
	 */
	void subscribe(Set<Integer> messageIds);
	
	/**
	 * Unsubscribe from a single message
	 * @param messageId
	 */
	void unsubscribe(int messageId);
	
	/**
	 * Query to see whether the client is subscribed to this message ID
	 * @param messageId
	 * @return
	 */
	boolean isSubscribed(int messageId);
	
	/**
	 * @return
	 */
	Set<Integer> getSubscriptions();

	// Methods that use message names

	/**
	 * @param messageName which message to subscribe to
	 * @return whether the messageName existed, resulting in a subscribe
	 */
	boolean subscribeByName(String messageName);
	
	/**
	 * Throw IO exception if MessageMap is null
	 * @param messageNames
	 * @return the number of messages successfully added as subscriptions
	 * 
	 */
	int subscribeByName(Set<String> messageNames);
	
	/**
	 * @param messageName
	 * @return whether the messageName existed, resulting in an unsubscribe
	 */
	boolean unsubscribeByName(String messageName);

	/**
	 * @param messageName
	 * @return
	 */
	boolean isSubscribed(String messageName);

	/**
	 * After calling this method, the client will not be subscribed to any messages
	 */
	void unsubscribeAll();

	/**
	 * This loop listens for the next incoming message and passes it to 
	 * @return Not currently returning anything, since this implemented as an
	 * infinite loop.
	 */
	boolean subscriberLoop();

}