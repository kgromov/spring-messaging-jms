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

//    @Async("taskExecutor")
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

//    @Scheduled(fixedRate = 2000)
    @SneakyThrows
    public void sendAndReceiveMessage() {

        HelloWorldMessage message = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("Hello")
                .build();

        Message receivedMsg = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE, session -> {
            try {
                Message helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                helloMessage.setStringProperty("_type", "guru.springframework.sfgjms.model.HelloWorldMessage");
                log.info("Sending Hello");
                return helloMessage;
            } catch (JsonProcessingException e) {
                throw new JMSException(e.getMessage());
            }
        });

        String body = receivedMsg.getBody(String.class);
        HelloWorldMessage receivedMessage = objectMapper.readerFor(HelloWorldMessage.class).readValue(body);
        log.info("send == received? {}", message.equals(receivedMessage));
        log.info(body);
    }

}
