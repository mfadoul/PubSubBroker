# PubSubBroker

A Java library that implements a publish-subscribe messaging scheme using ZeroMQ (ZMQ).

## Overview

PubSubBroker is a flexible and extensible Java implementation of the publish-subscribe messaging pattern built on top of ZeroMQ. It provides a central broker that manages message distribution between publishers and subscribers, enabling decoupled, asynchronous communication in distributed systems.

## Features

- **Pub-Sub Messaging**: Full publish-subscribe implementation with a central broker
- **ZeroMQ-Based**: Leverages ZeroMQ for efficient, fast message transport
- **Configuration Support**: Supports both JSON and XML configuration formats
- **UI Components**: Includes GUI applications for broker management and client interaction
- **Flexible Message Handling**: Supports various message formats and data structures
- **Extensible Architecture**: Clean separation of concerns with pluggable components
- **Comprehensive Testing**: Extensive test suite with JUnit integration

## Architecture

The PubSubBroker consists of several key components:

- **Broker**: Central message broker that manages subscriptions and message distribution
- **Publisher Clients**: Clients that publish messages to topics
- **Subscriber Clients**: Clients that subscribe to and receive messages from topics
- **Configuration System**: Flexible configuration management supporting JSON and XML formats
- **Message Handling**: Robust message serialization and deserialization

## Requirements

- Java 1.7 or higher
- ZeroMQ (ZMQ) library
- GSON 2.3.1 or higher (for JSON processing)
- JUnit 4 (for testing)
- jgoodies-forms 1.8.0 (for UI components)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/mfadoul/PubSubBroker.git
cd PubSubBroker
```

2. Ensure ZeroMQ is installed on your system:
```bash
# macOS
brew install zeromq

# Linux (Ubuntu/Debian)
sudo apt-get install libzmq3-dev

# Or compile from source: http://zeromq.org/intro:get-the-software
```

3. Build the project using Ant:
```bash
ant build
```

## Usage Examples

### Running the Broker

#### With Default Configuration (JSON):
```bash
ant "Broker (With Default JSON)"
```

#### With Custom Ports:
```bash
ant "Broker (With Default Ports)"
```
The default ports are 6000 (publisher port) and 6001 (subscriber port).

### Simple Publisher Example

```java
import zmq.pubsub.publisher.PublisherClient;

public class SimplePublisherTest {
    public static void main(String[] args) {
        // Create a publisher client
        PublisherClient publisher = new PublisherClient("tcp://localhost:6000");
        
        // Publish messages
        publisher.publish("topic", "Hello World");
        publisher.publish("topic", "This is a test message");
        
        // Close connection
        publisher.close();
    }
}
```

Run with:
```bash
ant "SimplePublisherTest (1)"
```

### Simple Subscriber Example

```java
import zmq.pubsub.subscriber.SubscriberClient;

public class SimpleSubscriberTest {
    public static void main(String[] args) {
        // Create a subscriber client
        SubscriberClient subscriber = new SubscriberClient("tcp://localhost:6001");
        
        // Subscribe to a topic
        subscriber.subscribe("topic");
        
        // Receive messages
        String message = subscriber.receive();
        System.out.println("Received: " + message);
        
        // Close connection
        subscriber.close();
    }
}
```

Run with:
```bash
ant "SimpleSubscriberTest (1)"
```

### JSON Configuration Example

Create a configuration file `broker-config.json`:
```json
{
  "brokerName": "MyBroker",
  "publisherPort": 6000,
  "subscriberPort": 6001,
  "topics": [
    {
      "name": "events",
      "description": "Event topic"
    },
    {
      "name": "alerts",
      "description": "Alert topic"
    }
  ]
}
```

Load and use:
```java
import zmq.pubsub.Broker;
import zmq.pubsub.configuration.PubSubBrokerConfiguration;

public class BrokerWithConfig {
    public static void main(String[] args) {
        // Load configuration from JSON
        PubSubBrokerConfiguration config = 
            PubSubBrokerConfiguration.loadFromJson("broker-config.json");
        
        // Create and start broker
        Broker broker = new Broker(config);
        broker.start();
    }
}
```

### Message Publishing with Map

```java
import zmq.pubsub.message.MessageMap;
import zmq.pubsub.publisher.PublisherClient;

public class MessageMapExample {
    public static void main(String[] args) {
        PublisherClient publisher = new PublisherClient("tcp://localhost:6000");
        
        // Create structured message
        MessageMap message = new MessageMap();
        message.put("sender", "client1");
        message.put("timestamp", System.currentTimeMillis());
        message.put("data", "Important data");
        
        // Publish message
        publisher.publish("events", message.toJson());
        
        publisher.close();
    }
}
```

## Testing

Run all tests:
```bash
ant test
```

Run specific test suites:
```bash
ant BrokerTest                          # Test broker functionality
ant SubscriberClientTest                # Test subscriber client
ant SubscriberClientJsonTest            # Test JSON-based subscriber
ant MessageMapTest                      # Test message mapping
ant MessageMapJsonTest                  # Test JSON message mapping
ant PubSubBrokerConfigurationTests      # Test configuration loading
ant PubSubBrokerConfigurationJsonTest   # Test JSON configuration
ant PubSubBrokerConfigurationXmlTest    # Test XML configuration
```

## UI Applications

The project includes several GUI applications:

### Broker UI
Manage the central broker with a graphical interface:
```bash
ant BrokerUI
```

### Publisher Client UI
Publish messages using a user-friendly interface:
```bash
ant PublisherClientUI
```

### Subscriber Client UI
Subscribe and receive messages with a graphical interface:
```bash
ant SubscriberClientUI
```

## Project Structure

```
PubSubBroker/
├── src/                          # Main source code
│   └── zmq/pubsub/              # Core PubSub implementation
│       ├── Broker.java          # Central broker
│       ├── publisher/           # Publisher components
│       ├── subscriber/          # Subscriber components
│       ├── message/             # Message handling
│       ├── configuration/       # Configuration management
│       └── ui/                  # GUI components
├── test/                         # Unit tests
├── examples/                     # Example implementations
│   └── zmq/pubsub/
│       └── simpletest/          # Simple test examples
├── doc/                          # Documentation
├── build.xml                     # Ant build configuration
├── README.md                     # This file
└── jgoodies-forms-1.8.0.jar     # UI library dependency
```

## Configuration

### JSON Configuration Format

```json
{
  "brokerName": "PubSubBroker",
  "publisherPort": 6000,
  "subscriberPort": 6001,
  "topics": []
}
```

### XML Configuration Format

```xml
<?xml version="1.0" encoding="UTF-8"?>
<brokerConfiguration>
    <brokerName>PubSubBroker</brokerName>
    <publisherPort>6000</publisherPort>
    <subscriberPort>6001</subscriberPort>
    <topics/>
</brokerConfiguration>
```

## Common Use Cases

### Real-time Event Distribution
Distribute events from multiple sources to interested subscribers in real-time.

### Sensor Data Collection
Collect sensor data from distributed IoT devices to a central processing system.

### System Monitoring
Monitor multiple systems and alert on specific conditions or events.

### Application Logging
Centralize log collection from distributed application instances.

### Command Broadcasting
Broadcast commands or configuration updates to multiple services.

## Troubleshooting

### ZeroMQ Connection Issues
- Ensure ZeroMQ library is properly installed and accessible
- Check that ports are not already in use
- Verify firewall settings allow communication on broker ports

### Build Failures
- Ensure Java 1.7+ is installed: `java -version`
- Check that all dependencies are in the correct paths
- Update `ECLIPSE_HOME` path in `build.xml` if using Eclipse integration

### Message Loss
- Ensure broker is running before publishers/subscribers connect
- Check network connectivity between broker and clients
- Review configuration for topic subscriptions

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## License

Please refer to the repository for license information.

## Support

For issues, questions, or discussions, please open an issue on the GitHub repository.
