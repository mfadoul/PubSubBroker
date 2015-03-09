package zmq.pubsub.message;

public abstract class Message {

	public Message(int messageId) {
		this.messageId=messageId;
	}
	
	// Private contents
	int messageId;

}
