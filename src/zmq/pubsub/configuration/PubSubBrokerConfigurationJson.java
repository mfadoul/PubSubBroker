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
		List<BrokerConfiguration> tempBrokerBindings = new ArrayList<BrokerConfiguration>();
		List<BrokerConfiguration> tempBrokerConnections = new ArrayList<BrokerConfiguration>();
		
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
						BrokerConfiguration brokerConfiguration = readBrokerConfiguration(jsonReader);
						if (brokerConfiguration != null) {
							tempBrokerBindings.add(brokerConfiguration);
						}
					}
					jsonReader.endArray();
				} else if ("brokerConnections".equals(name)) {
					// Found brokerConnections
					jsonReader.beginArray();
					while (jsonReader.hasNext()) {
						BrokerConfiguration brokerConfiguration = readBrokerConfiguration(jsonReader);
						if (brokerConfiguration != null) {
							tempBrokerConnections.add(brokerConfiguration);
						}
					}
					jsonReader.endArray();
				} else {
					// Mismatch
				}
			}
			jsonReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.brokerName = tempBrokerName;
		this.brokerBindings = tempBrokerBindings;
		this.brokerConnections = tempBrokerConnections;

		System.out.println("Broker Name = " + brokerName);
		System.out.println("Number of brokerBindings = " + brokerBindings.size());
		System.out.println("Number of brokerConnections = " + brokerConnections.size());
	}

	private BrokerConfiguration readBrokerConfiguration(JsonReader jsonReader) throws IOException {
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
			return new BrokerConfiguration(nameString, publisherEndpointString, subscriberEndpointString);
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
	public BrokerConfiguration getBrokerBinding(int index) {
		return brokerBindings.get(index);
	}

	@Override
	public int getBrokerConnectionCount() {
		return brokerConnections.size();
	}

	@Override
	public BrokerConfiguration getBrokerConnection(int index) {
		return brokerConnections.get(index);
	}

	@Override
	public String toString() {
		return "PubSubBrokerConfigurationJson [brokerName=" + brokerName + ", brokerBindings=" + brokerBindings
				+ ", brokerConnections=" + brokerConnections + "]";
	}
	
	// Members
	private final String brokerName;
	private final List<BrokerConfiguration> brokerBindings;
	private final List<BrokerConfiguration> brokerConnections;
}
