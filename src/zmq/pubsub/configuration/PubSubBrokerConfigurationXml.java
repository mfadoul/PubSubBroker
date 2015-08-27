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

	public PubSubBrokerConfigurationXml(String configFilename) {
		 // Add a handler for XML files.
		String tempBrokerName = "Broker";
		List<BrokerConnection> tempBrokerBindings = new ArrayList<BrokerConnection>();
		List<BrokerConnection> tempBrokerConnections = new ArrayList<BrokerConnection>();

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
						BrokerConnection brokerConfiguration = this.readBrokerConfiguration(brokerBindingsNodeList.item(i));
						if (brokerConfiguration != null) {
							tempBrokerBindings.add(brokerConfiguration);
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
						BrokerConnection brokerConfiguration = this.readBrokerConfiguration(brokerExternalConnectionsNodeList.item(j));
						if (brokerConfiguration != null) {
							tempBrokerConnections.add(brokerConfiguration);
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
		} catch (IOException e) {
			System.err.println("IO Exception when parsing configuration file.");
			e.printStackTrace();
		} 
		
		this.brokerName = tempBrokerName;
		this.brokerBindings = tempBrokerBindings;
		this.brokerExternalConnections = tempBrokerConnections;

		System.out.println("Broker Name = " + brokerName);
		System.out.println("Number of brokerBindings = " + brokerBindings.size());
		System.out.println("Number of brokerExternalConnections = " + brokerExternalConnections.size());
	}

	private BrokerConnection readBrokerConfiguration(Node node) {
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
			return new BrokerConnection(nameString, publisherEndpointString, subscriberEndpointString);
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
	public BrokerConnection getBrokerBinding(int index) {
		return brokerBindings.get(index);
	}

	@Override
	public int getBrokerExternalConnectionCount() {
		return brokerExternalConnections.size();
	}

	@Override
	public BrokerConnection getBrokerExternalConnection(int index) {
		return brokerExternalConnections.get(index);
	}

	@Override
	public String toString() {
		return "PubSubBrokerConfigurationXml [brokerName=" + brokerName + ", brokerBindings=" + brokerBindings
				+ ", brokerExternalConnections=" + brokerExternalConnections + "]";
	}

	// Members
	private final String brokerName;
	private final List<BrokerConnection> brokerBindings;
	private final List<BrokerConnection> brokerExternalConnections;
}
