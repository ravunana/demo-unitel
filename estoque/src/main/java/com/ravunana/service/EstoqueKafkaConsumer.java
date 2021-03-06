package com.ravunana.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ravunana.config.KafkaProperties;
import com.ravunana.service.dto.ProdutoDTO;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class EstoqueKafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(EstoqueKafkaConsumer.class);

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public static final String TOPIC = "topic_estoque";

    private final KafkaProperties kafkaProperties;

    private KafkaConsumer<String, String> kafkaConsumer;

    @Autowired
    private ProdutoService produtoService;

    public EstoqueKafkaConsumer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public void start() {
        log.info("Kafka consumer starting...");
        this.kafkaConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        Thread consumerThread = new Thread(() -> {
            try {
                kafkaConsumer.subscribe(Collections.singletonList(TOPIC));
                log.info("Kafka consumer started");
                while (!closed.get()) {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));
                    for (ConsumerRecord<String, String> record : records) {

                    	Gson gson = new Gson();
                        GsonBuilder builder = new GsonBuilder();
                        Gson g = builder.create();
                        
                        JsonObject jsonObject = gson.fromJson( record.value(), JsonObject.class);
                        int quantidade = jsonObject.get("produtoQuantidade").getAsInt();
                        Long codigo = jsonObject.get("produtoCodigo").getAsLong();

                        Optional<ProdutoDTO> produto = produtoService.findOne(codigo);
                        int estoque = produto.get().getEstoque() - quantidade;
                        produto.get().setEstoque(estoque);
                        produtoService.save(produto.get());

                        log.info("Consumed message in {} : {}", TOPIC, record.value());
                    }
                }
                kafkaConsumer.commitSync();
            } catch (WakeupException e) {
                // Ignore exception if closing
                if (!closed.get()) throw e;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                kafkaConsumer.close();
            }
        });
        consumerThread.start();
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void shutdown() {
        log.info("Shutdown Kafka consumer");
        closed.set(true);
        kafkaConsumer.wakeup();
    }
}
