package ru.ivanov.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class QueryManager {
    private static final Map<String, String> queries = new HashMap<>();
    static {
        String fileName = "sql_queries.properties";
        try (InputStream input = QueryManager.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Не удалось найти файл ресурсов с запросами: " + fileName);
            }
            Properties properties = new Properties();
            properties.load(input);
            properties.forEach((key, value) -> queries.put((String) key, (String) value));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении SQL запросов", e);
        }
    }

    public static String get(String queryKey) {
        String query = queries.get(queryKey);
        if (query == null) {
            throw new IllegalArgumentException("SQL запрос с ключом '" + queryKey + "' не найден в sql_queries.properties");
        }
        return query;
    }
}
