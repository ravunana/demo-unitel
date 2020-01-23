package com.ravunana.web.rest;

import com.ravunana.service.EstoqueKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estoque-kafka")
public class EstoqueKafkaResource {

    private final Logger log = LoggerFactory.getLogger(EstoqueKafkaResource.class);

    private EstoqueKafkaProducer kafkaProducer;

    public EstoqueKafkaResource(EstoqueKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.send(message);
    }
}
