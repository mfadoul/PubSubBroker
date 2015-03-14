Project: PubSubBroker
Creation Date: March 1, 2015
Author: Matthew Fadoul
E-mail: matthew.fadoul@gmail.com

----------------------------------
Tasks
1) Create a mechanism to launch with a configuration file.
2) Create adapter for SMM-functionality to use ZeroMQ.

----------------------------------
Configuration Data:

1) Publisher Port(2)
  * publisherPort = 5556
  
2) Subscriber Port(s)
  * subscriberPort = 5555

3) Messages
  
  
<Messages>

----------------------------------
Running from the bash shell

* Set up the Environment:
export DYLD_LIBRARY_PATH=/usr/local/lib

* Running the Broker:
java -classpath "/usr/local/share/java/zmq.jar:PubSubBroker.jar" zmq.pubsub.Broker

* Running the Subscriber:
java -classpath "/usr/local/share/java/zmq.jar:PubSubBroker.jar" zmq.pubsub.simpletest.SimpleSubscriberTest

* Running the Publisher:
java -classpath "/usr/local/share/java/zmq.jar:PubSubBroker.jar" zmq.pubsub.simpletest.SimplePublisherTest