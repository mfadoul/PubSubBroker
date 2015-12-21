package zmq.pubsub;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class should only have static methods.  No need for a public constructor.
 *
 */
public class MessageUtils {
	/**
	 * Convert a byte array into an equivalent integer, assuming the integer is
	 * stored as big-endian
	 * @param b Byte array that contains a big-endian representation of the integer
	 * @return
	 */
	public static int byteArrayToInt(byte[] b) {
		final ByteBuffer bb = ByteBuffer.wrap(b);
		bb.order(ByteOrder.BIG_ENDIAN);
		return bb.getInt();
	}

	/**
	 * Convert an integer into an equivalent byte array using big-endian notation.
	 * @param i an integer to be converted to a byte array.
	 * @return A byte array that contains a big-endian representation of the integer
	 */
	public static byte[] intToByteArray(int i) {
		final ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.putInt(i);
		return bb.array();
	}
}
