package zmq.pubsub.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.stream.JsonReader;

// 
/**
 * This class inherits from the MessageMap.  The extra functionality here is for
 * Loading the MessageMap from JSON.
 * Question: Is there a better design pattern for separating the base functionality
 *   from the JSON loader methods?
 */
public class MessageMapJson extends MessageMap {

	/**
	 * @param jsonInputStream
	 */
	public MessageMapJson(InputStream jsonInputStream) {
		try {
			int groupNumber = 0;
			JsonReader jsonReader = new JsonReader(new InputStreamReader(jsonInputStream, "UTF-8"));
			
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {
				String name = jsonReader.nextName();
				System.out.println("Name = " + name);
				if ("maxMessagesPerGroup".equals(name)) {
						this.maxMessagesPerGroup = jsonReader.nextInt();
				} else if ("messageGroups".equals(name)) {
					// Iterate through the groups
					jsonReader.beginArray();
					while (jsonReader.hasNext()) {
						readMessageGroup(jsonReader, groupNumber++);
					}
					jsonReader.endArray();
				} else {
					jsonReader.nextString();
				}
			}
	
			jsonReader.endObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param jsonReader
	 * @param groupNumber
	 * @throws IOException
	 */
	private void readMessageGroup(JsonReader jsonReader, int groupNumber) throws IOException {
		jsonReader.beginObject();

		int messageId = groupNumber * this.maxMessagesPerGroup;
				
		while (jsonReader.hasNext()) {
			String name = jsonReader.nextName();
			if ("groupName".equals(name)) {
				System.out.println ("Parsing messageGroup [" + jsonReader.nextString() + "]");
			} else if ("messages".equals(name)) {
				System.out.println("Found messages");
				jsonReader.beginArray();
				// Read array of messages in the group here
				while (jsonReader.hasNext()) {
					jsonReader.beginObject();
					while (jsonReader.hasNext()) {
						name = jsonReader.nextName();
						if ("messageName".equals(name)) {
							String messageName = jsonReader.nextString();
							if (messageName != null) {
								System.out.println("Adding message: " + messageName + " (id=" + messageId + ")");
								this.addMessage(messageId++, messageName);
							}
						}
					}
					jsonReader.endObject();					
				}
				jsonReader.endArray();
			}
		}
		jsonReader.endObject();
	}
	
	private int maxMessagesPerGroup;
	
}
