package com.example.spring.messaging.controllers;

import com.example.spring.messaging.model.HelloWorldMessage;
import com.example.spring.messaging.services.SyncReceiver;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PullMessageController {
    private final SyncReceiver syncReceiver;

    @GetMapping("pull")
    public HelloWorldMessage getMessage() {
        return syncReceiver.pullMessage();
    }
}
