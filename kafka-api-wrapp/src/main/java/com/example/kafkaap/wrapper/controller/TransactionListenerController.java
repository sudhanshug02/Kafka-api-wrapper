package com.example.kafkaap.wrapper.controller;

import com.example.kafkaap.wrapper.util.AESUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tx")
public class TransactionListenerController {

    private static final List<String> latestMessages = new ArrayList<>();

    @KafkaListener(
            topics = "${app.kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String encryptedMessage) {
        try {
            String decrypted = AESUtil.decrypt(
                    encryptedMessage,
                    "12345678901234567890123456789012"
            );
            synchronized (latestMessages) {
                if (latestMessages.size() > 20) {
                    latestMessages.remove(0);
                }
                latestMessages.add(decrypted);
            }
            System.out.println("[Consumer] Decrypted message: " + decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/listen")
    public List<Map<String, Object>> getLatestMessages() {
        List<Map<String, Object>> parsedMessages = new ArrayList<>();
        synchronized (latestMessages) {
            for (String msg : latestMessages) {
                try {
                    Map<String, Object> parsed = new ObjectMapper()
                            .readValue(msg, Map.class);
                    parsedMessages.add(parsed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return parsedMessages;
    }
}
