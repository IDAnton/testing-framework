package ru.ivanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.MDC;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.ivanov.queues.Message;
import ru.ivanov.queues.OrderWorker;
import ru.ivanov.tools.AllureTraceExtension;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@Testcontainers
@ExtendWith(AllureTraceExtension.class)
public class KafkaStepClientTest {
    private static final String MAIN_TOPIC = "order-events";
    private static final String DLQ_TOPIC = "order-events-dlq";

    @Container
    private static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    private OrderWorker worker;
    private KafkaProducer<String, String> testProducer;
    private KafkaConsumer<String, String> testDlqConsumer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        String bootstrapServers = kafka.getBootstrapServers();

        worker = new OrderWorker(bootstrapServers, MAIN_TOPIC, DLQ_TOPIC);
        worker.start();

        Properties prodProps = new Properties();
        prodProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        prodProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prodProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        testProducer = new KafkaProducer<>(prodProps);

        Properties consProps = new Properties();
        consProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-dlq-validator-group");
        consProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        testDlqConsumer = new KafkaConsumer<>(consProps);
        testDlqConsumer.subscribe(Collections.singletonList(DLQ_TOPIC));
    }

    @AfterEach
    void tearDown() {
        worker.stop();
        testProducer.close();
        testDlqConsumer.close();
    }

    @Test
    void testTraceId() throws Exception {
        String orderId = UUID.randomUUID().toString();
        String currentTraceId = MDC.get("traceId");

        Message invalidMessage = new Message(orderId, "INVALID");
        String jsonPayload = objectMapper.writeValueAsString(invalidMessage);
        ProducerRecord<String, String> record = ru.ivanov.tools.KafkaStepClientTest.createRecordWithTrace(MAIN_TOPIC, orderId, jsonPayload);
        testProducer.send(record).get();
        AtomicReference<ConsumerRecord<String, String>> receivedDlqRecord = new AtomicReference<>();
        await()
                .atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofMillis(200))
                .untilAsserted(() -> {
                    ConsumerRecords<String, String> records = testDlqConsumer.poll(Duration.ofMillis(200));
                    assertThat(records.isEmpty()).as("Сообщение еще не дошло до DLQ").isFalse();
                    receivedDlqRecord.set(records.iterator().next());
                });
        var traceHeaderBytes = receivedDlqRecord.get().headers().lastHeader("X-Trace-Id").value();
        String actualTraceIdInDlq = new String(traceHeaderBytes);

        assertThat(actualTraceIdInDlq)
                .as("TraceId должен совпадать")
                .isEqualTo(currentTraceId);
    }
}
