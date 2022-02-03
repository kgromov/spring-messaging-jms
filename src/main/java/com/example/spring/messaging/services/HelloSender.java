package com.example.spring.messaging.services;

import com.example.spring.messaging.config.JmsConfig;
import com.example.spring.messaging.model.HelloWorldMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.utils.RandomUtil;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Slf4j
public class HelloSender {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    private final MessageConverter messageConverter;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {

        HelloWorldMessage message = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("Hello World "+ RandomUtil.randomInterval(0, 100) + "!")
                .build();

        jmsTemplate.convertAndSend(message);
        log.info("Send message: {}", message);
    }

    @Scheduled(fixedRate = 2000)
    @SneakyThrows
    public void sendAndReceiveMessage() {

        HelloWorldMessage message = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("Hello")
                .build();


        Message receivedMsg = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE, withJacksonSerialization(message));

        String body = receivedMsg.getBody(String.class);
//        HelloWorldMessage receivedMessage = objectMapper.readerFor(HelloWorldMessage.class).readValue(body);
        HelloWorldMessage receivedMessage = (HelloWorldMessage) messageConverter.fromMessage(receivedMsg);
        log.info("send == received? {}", message.equals(receivedMessage));
        log.info(body);
    }

    private MessageCreator withJacksonSerialization(HelloWorldMessage message) {
        return session -> {
            try {
                Message helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                helloMessage.setStringProperty("_type", "com.example.spring.messaging.model.HelloWorldMessage");
                log.info("Sending Hello");
                return helloMessage;
            } catch (JsonProcessingException e) {
                throw new JMSException(e.getMessage());
            }
        };
    }

    private MessageCreator withMessageConvertor(HelloWorldMessage message) {
        return session -> {
            Message toMessage = messageConverter.toMessage(message, session);
            return toMessage;
        };
    }

}
