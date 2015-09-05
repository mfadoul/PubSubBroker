package zmq.pubsub.subscriber;

public abstract class SubscriberClientJson extends SubscriberClient {
	// Constructors
	public SubscriberClientJson (String subscriberConfigFilename) {
		this(new SubscriberData.Builder().jsonInputFilename(
				subscriberConfigFilename).build());
	}
	
	// After using the SubscriberData object's builder.
	public SubscriberClientJson (SubscriberData subscriberData) {
		super(subscriberData.getSubscriberEndpoint(),
				subscriberData.getMessageMap(),
				subscriberData.getMessageIds()
				);
	}
}
