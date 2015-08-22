package zmq.pubsub.configuration;

public class BrokerConfiguration {
	public BrokerConfiguration(String name, String publisherEndpoint, String subscriberEndpoint) {
		super();
		this.name = name;
		this.publisherEndpoint = publisherEndpoint;
		this.subscriberEndpoint = subscriberEndpoint;
	}
	
	// Getters
	public String getName() {
		return name;
	}
	public String getPublisherEndpoint() {
		return publisherEndpoint;
	}
	public String getSubscriberEndpoint() {
		return subscriberEndpoint;
	}

	private final String name;
	private final String publisherEndpoint;
	private final String subscriberEndpoint;
	
	@Override
	public String toString() {
		return "BrokerConfiguration [name=" + name + ", publisherEndpoint=" + publisherEndpoint
				+ ", subscriberEndpoint=" + subscriberEndpoint + "]";
	}
}
