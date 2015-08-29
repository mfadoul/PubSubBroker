package zmq.pubsub.configuration;

public interface PubSubBrokerConfiguration {
	// General parameters
	String getName();
	
	// Brokers
	int getBrokerBindingsCount();
	BrokerEndpointPair getBrokerBinding(int index);
	
	//Connections (i.e. other brokers started by others)
	int getBrokerExternalConnectionCount();
	BrokerEndpointPair getBrokerExternalConnection(int index);

}
