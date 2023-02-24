package com.example.spring.messaging.services;

import com.example.spring.messaging.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.Session;
import java.util.stream.Collectors;

import static com.example.spring.messaging.config.JmsConfig.TOPIC_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelloReceiver {
    @JmsListener(destination = TOPIC_NAME)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers,
                       Message message,
                       Session session) {

        log.info("Received message {}", helloWorldMessage);
        log.info("Headers: {}", headers.entrySet().stream().map(e -> e.getKey() + "="+e.getValue()).collect(Collectors.joining("\n")));
    }

    /*
     @JmsListener(destination = ORDER_TOPIC, containerFactory = "topicListenerFactory")
    public void receiveTopicMessage(@Payload Order order,
                                    @Headers MessageHeaders headers,
                                    Message message,
                                    Session session) {

        log.info("received <" + order + ">");
    }
     */
}
