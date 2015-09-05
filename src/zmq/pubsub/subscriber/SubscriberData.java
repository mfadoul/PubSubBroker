package zmq.pubsub.subscriber;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.stream.JsonReader;

import zmq.pubsub.message.MessageMap;
import zmq.pubsub.message.MessageMapJson;

public final class SubscriberData {
	private final String subscriberEndpoint;
	
	// The purpose of this set of messageIds is to provide access to the list
	// of subscriptions, since the subscriptions are not visible when querying 
	// the ZeroMQ Socket object.
	private final Set<Integer> messageIds;
		
	// This is a map of messageIds to messageNames
	private final MessageMap messageMap;

	public static class Builder {
		private final String defaultSubscriberEndpoint = "tcp://127.0.0.1:6001";
		
		// Optional parameters, initialized to default values.
		private String subscriberEndpoint = defaultSubscriberEndpoint;
		private Set<Integer> messageIds=new HashSet<Integer>();		
		private MessageMap messageMap = null;

		public Builder() {
		}
		
		public Builder subscriptionEndpoint(String val) {
			this.subscriberEndpoint=val;
			return this;
		}

		public Builder messageIds(Set<Integer> val) {
			this.messageIds=val;
			return this;
		}

		public Builder messageMap(MessageMap val) {
			this.messageMap=val;
			return this;
		}

		// Initialize with JSON input
		public Builder jsonInputFilename (String jsonInputFilename) {
			File jsonInputFile = new File(jsonInputFilename);
			InputStream jsonInputStream;
			try {
				jsonInputStream = new FileInputStream(jsonInputFile);
				this.jsonInputStream(jsonInputStream);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this;
		}

		// Initialize with JSON input
		public Builder jsonInputStream (InputStream jsonInputStream) {
			JsonReader jsonReader = null;

			String tempSubscriberName = null;
			String tempSubscriberEndpoint = null;
			String tempMessageMapFile = null;
			String tempDescription = null;
			try {
				jsonReader = new JsonReader(new InputStreamReader(jsonInputStream, "UTF-8"));
				// Read the name in the pubsubbroker
				jsonReader.beginObject();
				while (jsonReader.hasNext()) {
					String name = jsonReader.nextName();
					if ("name".equals(name)) {
						tempSubscriberName = jsonReader.nextString();
						System.out.println("Name of the subscriber = " + tempSubscriberName);
					} else if ("description".equals(name)) {
						tempDescription = jsonReader.nextString();
						System.out.println("Subscription description = " + tempDescription);
					} else if ("messageMapFile".equals(name)) {
						tempMessageMapFile = jsonReader.nextString();
						System.out.println("Filename of the messageMap = " + tempMessageMapFile);
					} else if ("subscriberEndpoint".equals(name)) {
						tempSubscriberEndpoint = jsonReader.nextString();
						System.out.println("Subscriber Endpoint = " + tempSubscriberEndpoint);
					} else if ("messages".equals(name)) {
						// Found brokerBindings
						jsonReader.beginArray();
						while (jsonReader.hasNext()) {
							// Read each messageName or MessageId.
							if (readMessageFromJson(jsonReader)) {
								// New message subscription
							} else {
								// Failed to subscribe to a new message for some reason.
							}
						}
						jsonReader.endArray();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (tempMessageMapFile != null) {
				try {
					File initialFile = new File(tempMessageMapFile);
					InputStream inputStream;
					inputStream = new FileInputStream(initialFile);
					messageMap = new MessageMapJson(inputStream);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					// Technically, the application can survive this, so we don't need to throw
					// an IllegalStateException.
				}
				
			}
			this.subscriberEndpoint=tempSubscriberEndpoint;
			
			return this;
		}
		
		// Helper method for reading from JSON
		// This reads a message entry in the subscription list file.
		// @return whether the subscriber could successfully subscribe to a new messageId.
		private boolean readMessageFromJson(JsonReader jsonReader) throws IOException {
			Integer messageId = null;
			String messageName = null;
			jsonReader.beginObject();

			while (jsonReader.hasNext()) {
				String name = jsonReader.nextName();
				if ("messageName".equals(name)) {
					messageName = jsonReader.nextString();
				} else if ("messageId".equals(name)) {
					messageId = jsonReader.nextInt();
				} else {
					jsonReader.skipValue();
				}
			}
			jsonReader.endObject();

			
			if (messageName != null) {
				// Get the messageId
				try {
					messageId = this.messageMap.getMessageId(messageName);
					return true;
				} catch (NullPointerException e) {
					// Fail if the messageMap is not set.
					// Still check below to see if the messageId was set.
				}
			}
			
			if (messageId != null) {
				messageIds.add(messageId);
				return true;
			} else {
				return false; // Fail if both messageId and messageName were not set.
			}
		}

		// Build method
		public SubscriberData build() {
			return new SubscriberData(this);
		}
		
	}
	
	// Private constructor is only accessible by builder
	private SubscriberData(Builder builder) {
		this.subscriberEndpoint = builder.subscriberEndpoint;
		this.messageIds = builder.messageIds;
		this.messageMap = builder.messageMap;
	}

	//Getters
	public String getSubscriberEndpoint() {
		return subscriberEndpoint;
	}

	public Set<Integer> getMessageIds() {
		return messageIds;
	}

	public MessageMap getMessageMap() {
		return messageMap;
	}
}
