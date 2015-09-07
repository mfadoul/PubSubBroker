package zmq.pubsub.configuration;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PubSubBrokerConfigurationXml implements PubSubBrokerConfiguration {

	public PubSubBrokerConfigurationXml(String configFilename) throws IOException {
		 // Add a handler for XML files.
		String tempBrokerName = "Broker";
		List<BrokerEndpointPair> tempBrokerBindings = new ArrayList<BrokerEndpointPair>();
		List<BrokerEndpointPair> tempBrokerConnections = new ArrayList<BrokerEndpointPair>();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			System.out.println("Parsing configuration file.");
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(configFilename));
	
			// Find the name of the pub sub broker
			try {
				Node pubSubBrokerNode = doc.getElementsByTagName("pubSubBroker").item(0);
				tempBrokerName=pubSubBrokerNode.getAttributes().getNamedItem("name").getNodeValue();
			} catch (NullPointerException e) {
				System.err.println("NPE: Couldn't set tempBrokerName");
			}
			// Find brokerBindings
			NodeList brokerBindingsTempList = doc.getElementsByTagName("brokerBindings");
			
			if (brokerBindingsTempList.getLength() > 0) {
				Node brokerBindingsNode = brokerBindingsTempList.item(0);
				System.out.println("brokerBindingsNode (toString) = " + brokerBindingsNode);
				NodeList brokerBindingsNodeList = brokerBindingsNode.getChildNodes();
				
				System.out.println("Expected number of broker bindings = " + brokerBindingsNodeList.getLength());

				for (int i = 0; i < brokerBindingsNodeList.getLength(); ++i) {
					if ("connection".equals(brokerBindingsNodeList.item(i).getNodeName())) {
						BrokerEndpointPair brokerEndpointPair = this.readBrokerEndpointPair(brokerBindingsNodeList.item(i));
						if (brokerEndpointPair != null) {
							tempBrokerBindings.add(brokerEndpointPair);
						}
					}
				}
			}
			
			// Find brokerExternalConnections
			NodeList brokerExternalConnectionsTempList = doc.getElementsByTagName("brokerExternalConnections");
			if (brokerExternalConnectionsTempList.getLength() > 0) {
				Node brokerExternalConnectionsNode = brokerExternalConnectionsTempList.item(0);
				NodeList brokerExternalConnectionsNodeList = brokerExternalConnectionsNode.getChildNodes();

				System.out.println("Expected number of brokerExternalConnections = " + brokerExternalConnectionsNodeList.getLength());

				for (int j = 0; j < brokerExternalConnectionsNodeList.getLength(); ++j) {
					if ("connection".equals(brokerExternalConnectionsNodeList.item(j).getNodeName())) {
						BrokerEndpointPair brokerEndpointPair = this.readBrokerEndpointPair(brokerExternalConnectionsNodeList.item(j));
						if (brokerEndpointPair != null) {
							tempBrokerConnections.add(brokerEndpointPair);
						}
					}
				}				
			}
		} catch (ParserConfigurationException e) {
			System.err.println("Unable to parse configuration file.");
			e.printStackTrace();
		} catch (SAXException e) {
			System.err.println("SAX Exception when parsing configuration file.");
			e.printStackTrace();
		}
		
		this.brokerName = tempBrokerName;
		this.brokerBindings = tempBrokerBindings;
		this.brokerExternalConnections = tempBrokerConnections;

		System.out.println("Broker Name = " + brokerName);
		System.out.println("Number of brokerBindings = " + brokerBindings.size());
		System.out.println("Number of brokerExternalConnections = " + brokerExternalConnections.size());
	}

	private BrokerEndpointPair readBrokerEndpointPair(Node node) {
		if (node==null)
			return null;
		
		String nameString = null;
		String publisherEndpointString = null;
		String subscriberEndpointString = null;

		NamedNodeMap namedNodeMap = node.getAttributes();
		
		nameString = namedNodeMap.getNamedItem("name").getNodeValue();
		publisherEndpointString = namedNodeMap.getNamedItem("publisherEndpoint").getNodeValue();
		subscriberEndpointString = namedNodeMap.getNamedItem("subscriberEndpoint").getNodeValue();
		
		if ((nameString != null) && publisherEndpointString != null && subscriberEndpointString != null) {
			return new BrokerEndpointPair(nameString, publisherEndpointString, subscriberEndpointString);
		} else {
			System.err.println("Error inside the Broker Configuration Reader");
		}
		return null;
	}
	
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
		return "PubSubBrokerConfigurationXml [brokerName=" + brokerName + ", brokerBindings=" + brokerBindings
				+ ", brokerExternalConnections=" + brokerExternalConnections + "]";
	}

	// Members
	private final String brokerName;
	private final List<BrokerEndpointPair> brokerBindings;
	private final List<BrokerEndpointPair> brokerExternalConnections;
}
