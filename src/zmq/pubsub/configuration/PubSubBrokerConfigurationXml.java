package zmq.pubsub.configuration;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

public class PubSubBrokerConfigurationXml implements PubSubBrokerConfiguration {

	// TODO: Complete this XML config file parser
	public PubSubBrokerConfigurationXml(String configFilename) {
		// TODO Auto-generated constructor stub
		 // Add a handler for XML files.
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			System.out.println("Parsing configuration file.");
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(configFilename));
			
			NodeList nodeList = doc.getElementsByTagName("broker");
			System.out.println("Number of brokers in configuration file = " + nodeList.getLength());
			
			for (int i=0; i < nodeList.getLength(); ++i) {
				Node node = nodeList.item(i);
				System.out.println ("Broker node: " + node.getAttributes().getNamedItem("name"));
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

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBrokerBindingsCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BrokerConnection getBrokerBinding(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBrokerExternalConnectionCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BrokerConnection getBrokerExternalConnection(int index) {
		// TODO Auto-generated method stub
		return null;
	}

}
