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

