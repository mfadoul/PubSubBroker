package zmq.pubsub.subscriber;

public abstract class SubscriberClientJson extends SubscriberClient {
	/**
	 * Constructor that configures the subscriber client using a JSON file
	 * @param subscriberConfigFilename
	 */
	public SubscriberClientJson (String subscriberConfigFilename) {
		this(new SubscriberData.Builder().jsonInputFilename(
				subscriberConfigFilename).build());
	}
	
	/**
	 * Constructor that configures the subscriber client using the
	 * SubscriberData object's builder
	 * @param subscriberData
	 */
	public SubscriberClientJson (SubscriberData subscriberData) {
		super(subscriberData.getSubscriberEndpoint(),
				subscriberData.getMessageMap(),
				subscriberData.getMessageIds()
				);
	}
}
