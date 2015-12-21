package zmq.pubsub.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// A class for mapping between message IDs (integers) and message Names (Strings)
public class MessageMap {

	/**
	 * Constructor
	 */
	public MessageMap() {
	}

	/**
	 * Add an id/name pair to the message map
	 * @param messageId integer value for the new message
	 * @param messageName string representation of the message name
	 */
	public void addMessage(int messageId, String messageName) {
		// Before adding the message, remove any instances of the messageName
		// so that there are not any duplicates.
		try {
			// Find if there is a previous use of the messageName.
			int oldMessageId = this.getMessageId(messageName);
			this.map.remove(oldMessageId);
		} catch (IOException e) {
			// If the code gets here, that is a good thing, since it
			// means that the messageName hasn't yet been used.
		}
		
		map.put(messageId, messageName);
	}
	/**
	 * @param messageId
	 */
	public void removeMessage(int messageId) {
		map.remove(messageId);
	}
	
	/**
	 * @param messageId
	 * @return
	 * @throws IOException
	 */
	public String getMessageName(int messageId) throws IOException {
		final String name = map.get(messageId);
		if (name != null) {
			return name;
		} else {
			throw (new IOException("Could not find messageId [" + messageId + "]"));
		}
	}
	
	/**
	 * Hopefully, each messageName maps to a single messageId.
	 * @param messageName a string indicating the message name
	 * @return the first messageId that is associated with a messageName.
	 * @throws IOException
	 */
	public int getMessageId(String messageName) throws IOException {
		// Check for a null value
		if (messageName != null) {
			for (Map.Entry<Integer, String> entry: map.entrySet()) {
				if ((entry.getValue()!= null) && (entry.getValue().equals(messageName))) {
					return entry.getKey();
				}
			}
		} else {
			for (Map.Entry<Integer, String> entry: map.entrySet()) {
				// Support null keys
				if (entry.getValue() == null) {
					return entry.getKey();
				}
			}
		}
		
		throw (new IOException("Could not find messageName [" + messageName + "]"));
	}
	
	/**
	 * @param messageId
	 * @return true if the messageId exists as a key in the map
	 */
	public boolean hasMessageId(int messageId) {
		return map.containsKey(messageId);
	}
	
	/**
	 * @param messageName
	 * @return true if the messageName exists as a key in the map
	 */
	public boolean hasMessageName(String messageName) {
		return map.containsValue(messageName);
	}
	
	/**
	 * @return
	 */
	public Set<Integer> getAllMessageIds() {
		return this.map.keySet();
	}
	
	@Override
	public String toString() {
		return "MessageMap [map=" + map + "]";
	}

	private final Map<Integer, String> map = new HashMap<Integer, String>();
}
