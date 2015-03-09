package zmq.pubsub;

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
    	
    	broker = new Broker(xpubPort, xsubPort);
    	System.out.println("Initializing broker.");
    	
    	broker.initialize();
    	System.out.println("After initialization of broker.");
    	
    }

}
