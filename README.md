# Hello world project for Spring messaging

-----
##Resources:
[docker image for artemisMQ] (https://github.com/vromero/activemq-artemis-docker)

````
docker run -it --rm \
-p 8161:8161 \
-p 61616:61616 \
vromero/activemq-artemis
````
Default credentials ``artemis / simetraehcapa``

Monitoring can be found [here] (http://127.0.0.1:8161/)

## To configure embedded broker additional dependencies required. 
And also simple configuration to start embedded broker:
```
ActiveMQServer server = ActiveMQServers.newActiveMQServer(new ConfigurationImpl()
	.setPersistenceEnabled(false)
	.setJournalDirectory("target/data/journal")
	.setSecurityEnabled(false)
	.addAcceptorConfiguration("invm", "vm://0")
);

server.start();
```
## In order to use publisher-subscriber with this oldster the following properties should be set:   
```yaml
spring:
  jms:
    listener:
      concurrency: ${number}        # [2; minimum number of concurrent subscribers
      max-concurrency: ${number}    # [2; maximum number of concurrent subscribers
    pub-sub-domain: true
```
concurrency = 2 set for test purpose to demonstrate that message delivered to all subscribers.    
Then in message headers destination as a topic can be found - **jms_destination=ActiveMQTopic[hello-world-topic]**    
instead of queue - e.g. **jms_destination=ActiveMQQueue[hello-world-queue]**
