package ru.ivanov;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@EnableAsync
public class AsyncBlackBox {
    @Autowired
    private JdbcTemplate jdbc;

    @PostMapping("/trigger")
    @Async // Эмулируем асинхронную цепочку: HTTP -> Kafka -> Worker
    public CompletableFuture<ResponseEntity<Void>> trigger(
            @RequestHeader("X-Request-Id") String requestId) throws InterruptedException {

        Thread.sleep(1500); // Имитация задержки передачи по Kafka и обработки воркером

// Эмулируем запись "воркером" в БД (таблица events должна быть создана в БД)
        jdbc.update("INSERT INTO events (request_id, status) VALUES (?, 'PROCESSED')", requestId);

        return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
}
