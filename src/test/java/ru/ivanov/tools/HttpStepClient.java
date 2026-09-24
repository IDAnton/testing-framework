package ru.ivanov.tools;


import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.specification.RequestSpecification;
import org.slf4j.MDC;
import static io.restassured.RestAssured.given;

public class HttpStepClient {
    public static RequestSpecification givenWithTrace() {
        String currentTraceId = MDC.get("traceId");
        return given()
                .relaxedHTTPSValidation()
                .filter(new AllureRestAssured())
                .header("X-Trace-Id", currentTraceId != null ? currentTraceId : "no-trace")
                .log().all();
    }
}
