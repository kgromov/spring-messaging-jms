package com.example.spring.messaging.services;

import com.example.spring.messaging.model.HelloWorldMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.utils.RandomUtil;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.example.spring.messaging.config.JmsConfig.TOPIC_NAME;


@Component
@RequiredArgsConstructor
@Slf4j
public class HelloSender {
    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {

        HelloWorldMessage message = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("Hello World "+ RandomUtil.randomInterval(0, 100) + "!")
                .build();

        jmsTemplate.convertAndSend(TOPIC_NAME, message);
        log.info("Send message: {}", message);
    }
}
