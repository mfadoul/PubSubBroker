package zmq.pubsub.configuration;

public interface PubSubBrokerConfiguration {
	
	// Specify a file to load.
	//boolean loadConfiguration(String filename);
	
	// General parameters
	String getName();
	
	// Brokers
	int getBrokerBindingsCount();
	BrokerConnection getBrokerBinding(int index);
	
	//Connections (i.e. other brokers started by others)
	int getBrokerExternalConnectionCount();
	BrokerConnection getBrokerExternalConnection(int index);

}
