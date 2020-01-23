package com.ravunana.web.rest;

import com.ravunana.service.FacturacaoKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/facturacao-kafka")
public class FacturacaoKafkaResource {

    private final Logger log = LoggerFactory.getLogger(FacturacaoKafkaResource.class);

    private FacturacaoKafkaProducer kafkaProducer;

    public FacturacaoKafkaResource(FacturacaoKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.send(message);
    }
}
