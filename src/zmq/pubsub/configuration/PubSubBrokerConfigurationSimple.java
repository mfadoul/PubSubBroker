package zmq.pubsub.configuration;

public class PubSubBrokerConfigurationSimple implements PubSubBrokerConfiguration {

	public PubSubBrokerConfigurationSimple(int xpubPort, int xsubPort) {
		brokerConnection = new BrokerConnection("Simple", "tcp://*:" + xpubPort, "tcp://*:" + xsubPort);
	}

	@Override
	public String getName() {
		return "Simple Connection";
	}

	@Override
	public int getBrokerBindingsCount() {
		// The simple configuration has one external connections
		return 1;
	}

	@Override
	public BrokerConnection getBrokerBinding(int index) {
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
	public BrokerConnection getBrokerExternalConnection(int index) {
		// The simple configuration has no external connections.
		return null;
	}
	
	private final BrokerConnection brokerConnection;

}
