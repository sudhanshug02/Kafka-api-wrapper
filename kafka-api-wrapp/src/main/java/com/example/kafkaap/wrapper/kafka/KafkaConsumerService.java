package com.example.kafkaap.wrapper.kafka;

import com.example.kafkaap.wrapper.util.AESUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @Value("${app.encryption.secret}")
    private String secret;

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "group_json")
    public void consume(String message) {
        try {
            String decrypted = AESUtil.decrypt(message, secret);
            System.out.println("[Consumer] Decrypted message: " + decrypted);
            // TODO: validate checksum if included in payload or headers
        } catch (Exception e) {
            System.err.println("[Consumer] Failed to decrypt/consume message: " + e.getMessage());
        }
    }
}
