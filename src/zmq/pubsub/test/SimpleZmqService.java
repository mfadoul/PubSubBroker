package zmq.pubsub.test;
import org.zeromq.ZMQ;

public class SimpleZmqService {
    public static void main (String [] args)
    {
    	String zmqPort = "tcp://127.0.0.1:5000";
    	//int messageSize = 1;
    	int roundtripCount = 1234;

    	System.out.println("ZMQ Version = " + ZMQ.getMajorVersion() + "." 
    			+ ZMQ.getMinorVersion() + "." 
    			+ ZMQ.getPatchVersion());
    	
    	System.out.println("ZMQ String Version = " + ZMQ.getVersionString());
    	System.out.println("ZMQ Full Version = " + ZMQ.getFullVersion());

        ZMQ.Context ctx = ZMQ.context (1);
        ZMQ.Socket s = ctx.socket (ZMQ.REP);
 
        //  Add your socket options here.
        //  For example ZMQ_RATE, ZMQ_RECOVERY_IVL and ZMQ_MCAST_LOOP for PGM.

        s.bind (zmqPort);

        System.out.println("Starting ZMQ Service");
        for (int i = 0; i != roundtripCount; i++) {
            System.out.println ("Packet " + i);

            // Receive
            String requestString = s.recvStr();
			System.out.println("  Received: " + requestString);

			// Respond
            String responseString = "Response " + i + " [" + requestString + "]";
			System.out.println("  Sent: " + responseString);
			s.send(responseString);
        }
        System.out.println("Done");

        try {
            Thread.sleep (1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace ();
        }

    }
}
