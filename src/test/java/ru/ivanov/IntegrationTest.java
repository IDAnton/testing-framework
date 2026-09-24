package ru.ivanov;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ivanov.DbTools.DbManager;
import ru.ivanov.DbTools.PostgresConfigurator;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest(classes = {App.class, AsyncBlackBox.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
//    @LocalServerPort
//    private int port;
//    private static DbManager db;
//
//    @Autowired
//    void initDbHelper(JdbcTemplate jdbcTemplate) {
//        db = new DbManager(jdbcTemplate);
//    }
//
//    @Container
//    private static final PostgreSQLContainer<?> postgres = PostgresConfigurator.configurePostgres();
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        if (!postgres.isRunning()) postgres.start();
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
//    }
//
//    @BeforeAll
//    static void setupDatabase() {
//        DbManager.createTableEvents(postgres);
//    }
//
//    @BeforeEach
//    void setUp() {
//        RestAssured.port = port;
//        db.cleanTableEvents();
//    }
//
//    @Test
//    void testPositive() {
//        String uniqueRequestId = UUID.randomUUID().toString();
//        given()
//                .header(new Header("X-Request-Id", uniqueRequestId))
//                .when()
//                .post("/trigger")
//                .then()
//                .statusCode(200);
//
//        await()
//                .atMost(Duration.ofSeconds(5))
//                .pollInterval(Duration.ofMillis(200))
//                .untilAsserted(() -> {
//                    List<String> statuses = db.getEventStatuses(uniqueRequestId);
//                    assertThat(statuses).as("Проверка статуса в БД").contains("PROCESSED");
//                });
//    }

//    @AfterAll
//    // так как мы останавливаем БД, этот тест должен быть последним, но из-за этого мы не можем повесить @Test, получается не очень красиво
//    static void testNegativePostgresStop() {
//        String uniqueRequestId = UUID.randomUUID().toString();
//        given()
//                .header(new Header("X-Request-Id", uniqueRequestId))
//                .when()
//                .post("/trigger")
//                .then()
//                .statusCode(200);
//
//        if (postgres.isRunning()) {
//            postgres.stop();
//        }
//
//        assertThrows(ConditionTimeoutException.class, () -> {
//            await()
//                    .atMost(Duration.ofSeconds(5))
//                    .pollInterval(Duration.ofMillis(200))
//                    .untilAsserted(() -> {
//                        List<String> statuses = db.getEventStatuses(uniqueRequestId);
//                        assertThat(statuses).contains("PROCESSED");
//                    });
//        }, "БД должна быть остановлена, данные не записаны");
//    }
}
