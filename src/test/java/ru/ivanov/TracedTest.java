package ru.ivanov;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.ivanov.tools.AllureTraceExtension;
import ru.ivanov.tools.HttpStepClient;
import ru.ivanov.tools.TheInternetSpec;

@ExtendWith(AllureTraceExtension.class)
public class TracedTest {
    @Test
    void testPositiveHttpRequest() {
        HttpStepClient.givenWithTrace()
                .spec(TheInternetSpec.getSuccessSpec())
                .when()
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    void testNegativeDistributionFailureReport() {
        HttpStepClient.givenWithTrace()
                .spec(TheInternetSpec.getFailSpec())
                .when()
                .get("/status_codes/500")
                .then()
                .statusCode(200);
    }
}
