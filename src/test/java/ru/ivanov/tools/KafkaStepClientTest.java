package ru.ivanov.tools;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.MDC;

public class KafkaStepClientTest {
    public static <K, V> ProducerRecord<K, V> createRecordWithTrace(String topic, K key, V value) {
        String currentTraceId = MDC.get("traceId");
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
        record.headers().add(new RecordHeader("X-Trace-Id", (currentTraceId != null ? currentTraceId : "no-trace").getBytes()));
        return record;
    }
}
