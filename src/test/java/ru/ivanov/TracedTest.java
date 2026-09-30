package ru.ivanov;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import ru.ivanov.tools.AllureTraceExtension;
import ru.ivanov.tools.HttpStepClient;
import ru.ivanov.tools.TheInternetSpec;

@ExtendWith(AllureTraceExtension.class)
public class TracedTest {
    private static final Logger log = LoggerFactory.getLogger(TracedTest.class);

    @Test
    @Story("Успешный запрос")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Проверка успешного запроса к основному эндпоинту")
    void testPositiveHttpRequest() {
        HttpStepClient.givenWithTrace()
                .spec(TheInternetSpec.getSuccessSpec())
                .when()
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    @Story("Обработка сбоев")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Намеренно падающий тест")
    void testNegative() {
        log.info("Запуск теста который должен упасть, лог должен сохранится в Allure отчете");
        Response response = Allure.step("Отправка запроса на заведомо нерабочий эндпоинт", () ->
                HttpStepClient.givenWithTrace()
                        .spec(TheInternetSpec.getFailSpec())
                        .when()
                        .get()
        );
        Allure.step("Прикрепление ответа эндпоинта к отчету", () -> {
            Allure.addAttachment("Тело ответа сервера", "application/json", response.getBody().asPrettyString());
            Allure.addAttachment("Заголовки ответа", "text/plain", response.getHeaders().toString());
        });
        Allure.step("Валидация статус-кода ответа (Ожидаем 200)", () -> {
            response.then().statusCode(200);
        });
    }
}
