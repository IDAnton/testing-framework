package ru.ivanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
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
import ru.ivanov.queues.OrderWorkerConfigurer;
import ru.ivanov.tools.AllureTraceExtension;

import java.time.Duration;
import java.util.Collections;
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
        testProducer = OrderWorkerConfigurer.createKafkaProducer(bootstrapServers);
        testDlqConsumer = OrderWorkerConfigurer.createKafkaConsumer(bootstrapServers);
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
