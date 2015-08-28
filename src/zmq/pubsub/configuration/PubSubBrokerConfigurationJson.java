package zmq.pubsub.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;

public class PubSubBrokerConfigurationJson implements PubSubBrokerConfiguration {

	public PubSubBrokerConfigurationJson(InputStream jsonInputStream) {
		JsonReader jsonReader = null;
		
		String tempBrokerName = null;
		List<BrokerEndpointPair> tempBrokerBindings = new ArrayList<BrokerEndpointPair>();
		List<BrokerEndpointPair> tempBrokerConnections = new ArrayList<BrokerEndpointPair>();
		
		try {
			jsonReader = new JsonReader(new InputStreamReader(jsonInputStream, "UTF-8"));
			// Read the name in the pubsubbroker
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {
				String name = jsonReader.nextName();
				if ("name".equals(name)) {
					tempBrokerName = jsonReader.nextString();
					System.out.println("Name of the broker = " + tempBrokerName);
					
				} else if ("brokerBindings".equals(name)) {
					// Found brokerBindings
					jsonReader.beginArray();
					while (jsonReader.hasNext()) {
						BrokerEndpointPair brokerEndpointPair = readBrokerEndpointPair(jsonReader);
						if (brokerEndpointPair != null) {
							tempBrokerBindings.add(brokerEndpointPair);
						}
					}
					jsonReader.endArray();
				} else if ("brokerExternalConnections".equals(name)) {
					// Found brokerExternalConnections
					jsonReader.beginArray();
					while (jsonReader.hasNext()) {
						BrokerEndpointPair brokerEndpointPair = readBrokerEndpointPair(jsonReader);
						if (brokerEndpointPair != null) {
							tempBrokerConnections.add(brokerEndpointPair);
						}
					}
					jsonReader.endArray();
				} else {
					// Mismatch
				}
			}
			jsonReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.brokerName = tempBrokerName;
		this.brokerBindings = tempBrokerBindings;
		this.brokerExternalConnections = tempBrokerConnections;

		System.out.println("Broker Name = " + brokerName);
		System.out.println("Number of brokerBindings = " + brokerBindings.size());
		System.out.println("Number of brokerExternalConnections = " + brokerExternalConnections.size());
	}

	private BrokerEndpointPair readBrokerEndpointPair(JsonReader jsonReader) throws IOException {
		String nameString = null;
		String publisherEndpointString = null;
		String subscriberEndpointString = null;

		jsonReader.beginObject();

		while (jsonReader.hasNext()) {
			String name = jsonReader.nextName();
			if ("name".equals(name)) {
				nameString = jsonReader.nextString();
			} else if ("publisherEndpoint".equals(name)) {
				publisherEndpointString = jsonReader.nextString();
			} else if ("subscriberEndpoint".equals(name)) {
				subscriberEndpointString = jsonReader.nextString();
			} else {
				jsonReader.skipValue();
			}
		}
		jsonReader.endObject();

		if ((nameString != null) && publisherEndpointString != null && subscriberEndpointString != null) {
			return new BrokerEndpointPair(nameString, publisherEndpointString, subscriberEndpointString);
		} else {
			System.err.println("Error inside the Broker Configuration Reader");
		}
		return null;
	}

	// private List<BrokerConfiguration>
	@Override
	public String getName() {
		return brokerName;
	}

	@Override
	public int getBrokerBindingsCount() {
		return brokerBindings.size();
	}

	@Override
	public BrokerEndpointPair getBrokerBinding(int index) {
		return brokerBindings.get(index);
	}

	@Override
	public int getBrokerExternalConnectionCount() {
		return brokerExternalConnections.size();
	}

	@Override
	public BrokerEndpointPair getBrokerExternalConnection(int index) {
		return brokerExternalConnections.get(index);
	}

	@Override
	public String toString() {
		return "PubSubBrokerConfigurationJson [brokerName=" + brokerName + ", brokerBindings=" + brokerBindings
				+ ", brokerExternalConnections=" + brokerExternalConnections + "]";
	}
	
	// Members
	private final String brokerName;
	private final List<BrokerEndpointPair> brokerBindings;
	private final List<BrokerEndpointPair> brokerExternalConnections;
}
