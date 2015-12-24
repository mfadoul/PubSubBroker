Project: PubSubBroker
Creation Date: March 1, 2015
Author: Matthew Fadoul
E-mail: matthew.fadoul@gmail.com

----------------------------------
Default Configuration Data:

1) Publisher Port(2)
  * publisherPort = 6000
  
2) Subscriber Port(s)
  * subscriberPort = 6001

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

------------------------------------
Dependencies:
* ZeroMQ (Java Bindings)
  * zmq.jar (JZMQ)
  * https://github.com/zeromq/jzmq
* GSON 2.3.1
  * gson-2.3.1.jar
  * https://github.com/google/gson
* JGoodies Forms 1.8.0
  * jgoodies-forms-1.8.0.jar
  * http://www.jgoodies.com/freeware/libraries/forms/

