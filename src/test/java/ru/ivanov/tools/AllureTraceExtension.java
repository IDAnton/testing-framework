package ru.ivanov.tools;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;
import java.util.stream.Collectors;

public class AllureTraceExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final String TRACE_ID_KEY = "traceId";

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(TRACE_ID_KEY, traceId);

        Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        ListAppender<ILoggingEvent> memoryAppender = (ListAppender<ILoggingEvent>) rootLogger.getAppender("MEMORY");
        if (memoryAppender != null) {
            memoryAppender.list.clear();
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
            ListAppender<ILoggingEvent> memoryAppender = (ListAppender<ILoggingEvent>) rootLogger.getAppender("MEMORY");

            if (memoryAppender != null) {
                String formattedLogs = memoryAppender.list.stream()
                        .map(event -> String.format("[%s] [%s] %s - %s",
                                new java.util.Date(event.getTimeStamp()),
                                event.getMDCPropertyMap().getOrDefault(TRACE_ID_KEY, "no-trace"),
                                event.getLevel(),
                                event.getFormattedMessage()))
                        .collect(Collectors.joining("\n"));

                Allure.addAttachment("Логи теста: ", "text/plain", formattedLogs);
            }
        }
        MDC.remove(TRACE_ID_KEY);
    }
}
