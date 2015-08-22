package zmq.pubsub.configuration;

public interface PubSubBrokerConfiguration {
	
	// Specify a file to load.
	//boolean loadConfiguration(String filename);
	
	// General parameters
	String getName();
	
	// Brokers
	int getBrokerBindingsCount();
	BrokerConfiguration getBrokerBinding(int index);
	
	//Connections (i.e. other brokers started by others)
	int getBrokerConnectionCount();
	BrokerConfiguration getBrokerConnection(int index);

}