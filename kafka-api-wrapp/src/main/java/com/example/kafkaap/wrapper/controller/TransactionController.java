package com.example.kafkaap.wrapper.controller;

import com.example.kafkaap.wrapper.kafka.KafkaProducerService;
import com.example.kafkaap.wrapper.util.AESUtil;
import com.example.kafkaap.wrapper.util.ChecksumUtil;
import com.example.kafkaap.wrapper.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tx")
public class TransactionController {

    @Autowired
    private KafkaProducerService producer;

    @Value("${app.encryption.secret}")
    private String secret;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.kafka.topic}")
    private String topic;

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/send")
    public ResponseEntity<?> sendTransaction(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "Authorization", required = false) String auth,
            @RequestHeader(value = "Checksum", required = false) String checksum
    ) throws Exception {
        // 1. Validate JWT
        if (!JwtUtil.validateToken(auth, jwtSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT Token");
        }

        // 2. Compute checksum and compare
        String json = mapper.writeValueAsString(payload);
        String expected = ChecksumUtil.generateMD5(json);
        if (checksum == null || !expected.equalsIgnoreCase(checksum)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Checksum mismatch. Expected: " + expected);
        }

        // 3. Encrypt
        String encrypted = AESUtil.encrypt(json, secret);

        // 4. Send to Kafka
        producer.send(topic, encrypted);

        return ResponseEntity.ok(Map.of("status", "sent", "topic", topic));
    }
}
