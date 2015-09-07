package zmq.pubsub.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// A class for mapping between message IDs (integers) and message Names (Strings)
public class MessageMap {

	public MessageMap() {
	}

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
	public void removeMessage(int messageId) {
		map.remove(messageId);
	}
	
	public String getMessageName(int messageId) throws IOException {
		final String name = map.get(messageId);
		if (name != null) {
			return name;
		} else {
			throw (new IOException("Could not find messageId [" + messageId + "]"));
		}
	}
	
	// Returns the first messageId that is associated with a messageName.
	// Hopefully, each messageName maps to a single messageId.
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
	
	// Return true if the messageId exists as a key in the map.
	public boolean hasMessageId(int messageId) {
		return map.containsKey(messageId);
	}
	
	// Return true if the messageId exists as a key in the map.
	public boolean hasMessageName(String messageName) {
		return map.containsValue(messageName);
	}
	
	public Set<Integer> getAllMessageIds() {
		return this.map.keySet();
	}
	
	@Override
	public String toString() {
		return "MessageMap [map=" + map + "]";
	}

	private final Map<Integer, String> map = new HashMap<Integer, String>();
}
