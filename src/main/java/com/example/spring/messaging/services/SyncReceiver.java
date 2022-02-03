package com.example.spring.messaging.services;

import com.example.spring.messaging.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncReceiver {
    private final JmsTemplate jmsTemplate;

    public HelloWorldMessage pullMessage() {
        return (HelloWorldMessage) jmsTemplate.receiveAndConvert();
    }
}
