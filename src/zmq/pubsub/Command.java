package zmq.pubsub;

import java.util.HashMap;
import java.util.Map;

public enum Command {
	SendMessage(1),
	SendMessageWithFilter(2), // Filter receiver
	RegisterClientRequest(11),
	RegisterClientResponse(12),
	Subscribe(21),
	SubscribeSet(22),
	Unsubscribe(31),
	UnsubscribeAll(32),
	GetSubscribersRequest(41),
	GetSubscribersResponse(42);
	
	private static Map<Integer, Command> commandMap;
	
	public static Command getMessage(int i) {
        if (commandMap == null) {
        	// Initialize the map once
        	commandMap = new HashMap<Integer, Command>();
            for (Command c : values()) {
            	commandMap.put(c.id, c);
            }
        }
        return commandMap.get(i);
    }

	private int id;
	
	private Command (int id) {
		this.id = id;
	}
	
	public int getId () {
		return id;
	}
}
