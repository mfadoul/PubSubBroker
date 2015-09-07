package zmq.pubsub.configuration;

public class PubSubBrokerConfigurationSimple implements PubSubBrokerConfiguration {

	public PubSubBrokerConfigurationSimple(int xpubPort, int xsubPort) {
		brokerConnection = new BrokerEndpointPair("SimpleEndpointPair", 
				"tcp://*:" + Integer.toString(xpubPort), 
				"tcp://*:" + Integer.toString(xsubPort));
	}

	@Override
	public String getName() {
		return "SimplePubSubBroker";
	}

	@Override
	public int getBrokerBindingsCount() {
		// The simple configuration has one external connections
		return 1;
	}

	@Override
	public BrokerEndpointPair getBrokerBinding(int index) {
		if (index == 0) {
			return brokerConnection;
		}
		else
			return null;
	}

	@Override
	public int getBrokerExternalConnectionCount() {
		// The simple configuration has no external connections.
		return 0;
	}

	@Override
	public BrokerEndpointPair getBrokerExternalConnection(int index) {
		// The simple configuration has no external connections.
		return null;
	}
	
	private final BrokerEndpointPair brokerConnection;

}
