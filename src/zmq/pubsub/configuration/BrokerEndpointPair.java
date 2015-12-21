package zmq.pubsub.configuration;

/**
 *
 */
public class BrokerEndpointPair {
	/**
	 * Constructor
	 * @param name
	 * @param publisherEndpoint
	 * @param subscriberEndpoint
	 */
	public BrokerEndpointPair(String name, String publisherEndpoint, String subscriberEndpoint) {
		super();
		this.name = name;
		this.publisherEndpoint = publisherEndpoint;
		this.subscriberEndpoint = subscriberEndpoint;
	}
	
	/**
	 * @return the name of the endpoint pair
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the publisher endpoint
	 */
	public String getPublisherEndpoint() {
		return publisherEndpoint;
	}
	
	/**
	 * @return the subscriber endpoint
	 */
	public String getSubscriberEndpoint() {
		return subscriberEndpoint;
	}

	/**
	 * Local members
	 */
	private final String name;
	private final String publisherEndpoint;
	private final String subscriberEndpoint;
	
	@Override
	public String toString() {
		return "BrokerConnection [name=" + name + ", publisherEndpoint=" + publisherEndpoint
				+ ", subscriberEndpoint=" + subscriberEndpoint + "]";
	}
}
