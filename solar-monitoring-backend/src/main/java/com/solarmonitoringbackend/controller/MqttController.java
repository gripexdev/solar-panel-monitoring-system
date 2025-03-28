package com.solarmonitoringbackend.controller;

import com.solarmonitoringbackend.dto.MqttMessageDto;
import com.solarmonitoringbackend.service.MqttService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mqtt")
public class MqttController {
    private static final Logger logger = LoggerFactory.getLogger(MqttController.class);

    private final MqttService mqttService;

    public MqttController(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody MqttMessageDto messageDto) {
        try {
            if (messageDto.getTopic() == null || messageDto.getTopic().isEmpty()) {
                return ResponseEntity.badRequest().body("Topic is required");
            }

            mqttService.publishMessage(messageDto.getTopic(), messageDto.getMessage());
            return ResponseEntity.ok("Message published successfully to topic: " + messageDto.getTopic());
        } catch (Exception e) {
            logger.error("Failed to publish MQTT message", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to publish message: " + e.getMessage());
        }
    }
}