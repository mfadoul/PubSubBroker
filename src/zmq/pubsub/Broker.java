package zmq.pubsub;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Broker {

	public Broker(int xpubPort, int xsubPort) {
		Context context = ZMQ.context(1);
		
		this.xpubPort = xpubPort;
		this.xsubPort = xsubPort;
		
		this.xpubSocket = context.socket(ZMQ.XPUB);
		this.xsubSocket = context.socket(ZMQ.XSUB);
	}

	public Broker(String configFilename) {
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
	
	public boolean initialize () {
		this.xpubSocket.bind("tcp://*:" + xpubPort);
		this.xsubSocket.bind("tcp://*:" + xsubPort);
		
		// Note: ZeroMQ doesn't care which socket is associated with the "frontend" vs. "backend".
		ZMQ.proxy(xsubSocket, xpubSocket, null);
		System.out.println("After ZMQ.proxy()");
		
		initialized = true;
		return true;
	}
	
	public boolean isInitialized () {
		return initialized;
	}
	
	private Socket xpubSocket = null;
	private Socket xsubSocket = null;
	
	private int xpubPort = 0;
	private int xsubPort = 0;
	
	private boolean initialized = false;
	
    public static void main (String [] args) {
    	Broker broker = null;
    	
    	int xpubPort = 5555; // Default value
    	int xsubPort = 5556; // Default value
    	
    	System.out.println("Number of args = " + args.length);
    	if (args.length>=2) {
    		try {
				xpubPort = Integer.parseInt(args[0]);
				xsubPort = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("Unable to parse ports on the command line.");
				System.exit(1);
			}
    		
    	}
    	
    	System.out.println("xpubPort = " + xpubPort);
    	System.out.println("xsubPort = " + xsubPort);
    	
    	// broker = new Broker(xpubPort, xsubPort);
    	broker = new Broker("data/PubSubBroker.xml");
    	System.out.println("Initializing broker.");
    	
    	broker.initialize();
    	System.out.println("After initialization of broker.");
    	
    }

}
