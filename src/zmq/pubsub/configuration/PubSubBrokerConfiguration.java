package zmq.pubsub.configuration;

/**
 *
 */
public interface PubSubBrokerConfiguration {
	/**
	 * @return the name of the broker
	 */
	String getName();
	
	// Brokers
	int getBrokerBindingsCount();
	BrokerEndpointPair getBrokerBinding(int index);
	
	//Connections (i.e. other brokers started by others)
	int getBrokerExternalConnectionCount();
	BrokerEndpointPair getBrokerExternalConnection(int index);

}
