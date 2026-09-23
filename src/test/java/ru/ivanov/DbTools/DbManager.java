package ru.ivanov.DbTools;

import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class DbManager {

    private final JdbcTemplate jdbcTemplate;

    public DbManager(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void createTableEvents(PostgreSQLContainer<?> container) {
        try (Connection conn = container.createConnection("");
             Statement stmt = conn.createStatement()) {
            stmt.execute(QueryManager.get("CREATE_EVENTS_TABLE"));
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать таблицу events при старте контейнера", e);
        }
    }

    public List<String> getEventStatuses(String requestId) {
        return jdbcTemplate.queryForList(
                QueryManager.get("SELECT_STATUS_WITH_ID"),
                String.class,
                requestId
        );
    }

    public void cleanTableEvents() {
        jdbcTemplate.execute(QueryManager.get("CLEAR_EVENTS_TABLE"));
    }
}
