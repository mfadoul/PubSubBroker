package zmq.pubsub;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MessageUtils {

	public MessageUtils() {
		
	}

/*	public static byte[] intToByteArray (int inputInt) {
		return ByteBuffer.allocate(4).putInt(inputInt).array();
	}
	
	public static byte[] byteArrayToInt (byte[] inputByteArray) {
		return ByteBuffer.allocate(4).putInt(inputInt).array();
		Integer.
	}
*/
	public static int byteArrayToInt(byte[] b) {
	    final ByteBuffer bb = ByteBuffer.wrap(b);
	    bb.order(ByteOrder.BIG_ENDIAN);
	    return bb.getInt();
	}

	public static byte[] intToByteArray(int i) {
	    final ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
	    bb.order(ByteOrder.BIG_ENDIAN);
	    bb.putInt(i);
	    return bb.array();
	}

}
