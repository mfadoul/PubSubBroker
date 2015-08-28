package zmq.pubsub;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import zmq.pubsub.configuration.BrokerConnection;
import zmq.pubsub.configuration.PubSubBrokerConfiguration;
import zmq.pubsub.configuration.PubSubBrokerConfigurationJson;
import zmq.pubsub.configuration.PubSubBrokerConfigurationSimple;
import zmq.pubsub.configuration.PubSubBrokerConfigurationXml;

public class Broker {

	// Initialize a broker by specifying two TCP ports
	public Broker(int xpubPort, int xsubPort) {
		this.pubSubBrokerConfiguration = new PubSubBrokerConfigurationSimple(xpubPort, xsubPort);
	}

	// Initialize a broker using an XML Configuration file
	public Broker(String xmlConfigFilename) throws FileNotFoundException {
		this.pubSubBrokerConfiguration = new PubSubBrokerConfigurationXml(xmlConfigFilename);
		System.out.println("pubSubBroker object = " + this.pubSubBrokerConfiguration);		
	}
	
	// Initialize a broker using a JSON Configuration file
	public Broker(InputStream jsonInputStream) throws FileNotFoundException {
		this.pubSubBrokerConfiguration = new PubSubBrokerConfigurationJson(jsonInputStream);
		System.out.println("pubSubBroker object = " + this.pubSubBrokerConfiguration);		
	}

	public boolean initialize () {
		
		Context context = ZMQ.context(1);
		this.xpubSocket = context.socket(ZMQ.XPUB);
		this.xsubSocket = context.socket(ZMQ.XSUB);		

		System.out.println("Initializing Broker [" + this.pubSubBrokerConfiguration.getName() + "]");
		for (int index = 0; index < this.pubSubBrokerConfiguration.getBrokerBindingsCount(); ++index) {
			BrokerConnection brokerConnection = this.pubSubBrokerConfiguration.getBrokerBinding(index);
			
			// Please note: 
			// The "XSUB" is bound to the publisher endpoint(s).
			// The "XPUB" is bound to the subscriber endpoint(s).
			// This may appear a bit non-intuitive, but the XSUB is subscribing to the publishers,
			// while the XPUB is publishing to the subscribers.
			// For more information, see: http://zguide.zeromq.org/php:chapter2#toc13
			
			System.out.println("Publisher Socket: Binding to " + brokerConnection.getPublisherEndpoint());
			this.xsubSocket.bind(brokerConnection.getPublisherEndpoint());
			
			System.out.println("Subscriber Socket: Binding to " + brokerConnection.getSubscriberEndpoint());
			this.xpubSocket.bind(brokerConnection.getSubscriberEndpoint());
		}
		
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
		
	private boolean initialized = false;
	
	// Broker Configuration
	final PubSubBrokerConfiguration pubSubBrokerConfiguration;
	
    public static void main (String [] args) {
    	Broker broker = null;
    	    	
    	System.out.println("Number of args = " + args.length);
    	
    	switch (args.length) {
    	case 0:
    		// Use default configuration file
    		try {
    			broker = new Broker("data/PubSubBroker.json");
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
    		break;
    	case 1:
    		// Configuration file specified
    		try {
    			broker = new Broker(args[0]);
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
    		break;

    	case 2:
    		// Ports specified
    		try {
				int xpubPort = Integer.parseInt(args[0]);
				int xsubPort = Integer.parseInt(args[1]);
				
		    	System.out.println("xpubPort = " + xpubPort);
		    	System.out.println("xsubPort = " + xsubPort);
		    	
		    	broker = new Broker(xpubPort, xsubPort);
			} catch (NumberFormatException e) {
				System.err.println("Unable to parse ports on the command line.");
				System.exit(1);
			}
    		break;
   		default:
   			// Mismatch.  Help
   			System.err.println ("Commandline parameters not recognized.");
    		break;
    	}
    	
    	if (broker != null) {
        	System.out.println("Initializing broker.");
	    	broker.initialize();
	    	System.out.println("After initialization of broker.");
    	} else {
    		System.err.println("Couldn't create Broker object");
    	}
    }
}

