package ru.ivanov.tools;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresConfigurator {
    public static PostgreSQLContainer<?> configurePostgres() {
        return new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("test_db")
                .withUsername("test_user")
                .withPassword("test_password");
    }
}
