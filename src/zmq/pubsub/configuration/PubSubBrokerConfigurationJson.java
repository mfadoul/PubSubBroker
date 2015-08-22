package zmq.pubsub.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;

public class PubSubBrokerConfigurationJson implements PubSubBrokerConfiguration {

	public PubSubBrokerConfigurationJson(InputStream jsonInputStream) {
		JsonReader reader = null;
		this.brokerName = null;
		this.brokerBindings = new ArrayList<BrokerConfiguration>();
		this.brokerConnections = new ArrayList<BrokerConfiguration>();

		try {
			reader = new JsonReader(new InputStreamReader(jsonInputStream, "UTF-8"));
			// Read the name in the pubsubbroker
			reader.beginObject();
			while (reader.hasNext()) {
				String fieldName = reader.nextName();
				System.out.println("Name = " + fieldName);
				if ("pubSubBroker".equals(fieldName)) {
					System.out.println("Found the pubSubBroker Object");
					readPubSubBrokerConfiguration(reader);
				} else {
					System.out.println ("Skipping " + fieldName);
					reader.skipValue();
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Broker Name = " + brokerName);
		System.out.println("Number of brokerBindings = " + brokerBindings.size());
		System.out.println("Number of brokerConnections = " + brokerConnections.size());
	}

	private void readPubSubBrokerConfiguration(JsonReader jsonReader) throws IOException {
		// Read the broker
		jsonReader.beginObject();  // Begin pubSubBroker
		
		while (jsonReader.hasNext()) {
			String name = jsonReader.nextName();
			if ("name".equals(name)) {
				this.brokerName = jsonReader.nextString();
				System.out.println("Name of the broker = " + this.brokerName);
				
			} else if ("brokerBindings".equals(name)) {
				// Found brokerBindings
				jsonReader.beginArray();
				while (jsonReader.hasNext()) {
					BrokerConfiguration brokerConfiguration = readBrokerConfiguration(jsonReader);
					if (brokerConfiguration != null) {
						brokerBindings.add(brokerConfiguration);
					}
				}
				jsonReader.endArray();
			} else if ("brokerConnections".equals(name)) {
				// Found brokerConnections
				jsonReader.beginArray();
				while (jsonReader.hasNext()) {
					BrokerConfiguration brokerConfiguration = readBrokerConfiguration(jsonReader);
					if (brokerConfiguration != null) {
						brokerConnections.add(brokerConfiguration);
					}
				}
				jsonReader.endArray();
			} else {
				// Mismatch
			}
		}
		jsonReader.endObject();  // End pubSubBroker

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

	private String brokerName;
	private List<BrokerConfiguration> brokerBindings;
	private List<BrokerConfiguration> brokerConnections;
	@Override
	public String toString() {
		return "PubSubBrokerConfigurationJson [brokerName=" + brokerName + ", brokerBindings=" + brokerBindings
				+ ", brokerConnections=" + brokerConnections + "]";
	}
}
