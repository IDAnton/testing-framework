package ru.ivanov.cucumber;

import io.cucumber.java.en.*;
import io.restassured.response.Response;

import java.math.BigDecimal;

import ru.ivanov.cucumber.ApiModels.*;
import ru.ivanov.cucumber.ApiSpecifications.AuthManager;
import static org.hamcrest.Matchers.is;

public class ApiStepDefinitions {

    private String authToken;
    private int bookingId;
    private Response authResponse;
    private Response bookingResponse;
    private Response deleteResponse;

    @Given("выполнена успешная авторизация с логином {string} и паролем {string}")
    public void executeSuccessfulAuth(String username, String password) {
        AuthRequest request = new AuthRequest(username, password);
        authResponse = AuthManager.sendAuthRequest(request);
    }

    @Given("получен валидный токен авторизации")
    public void extractAuthToken() {
        authResponse.then().statusCode(200);
        authToken = authResponse.body().path("token");
    }

    @When("отправлен запрос на создание нового бронирования для пользователя {string} {string}")
    public void createNewBooking(String firstName, String lastName) {
        CreateBookingRequest bookingRequest = new CreateBookingRequest(
                firstName, lastName, BigDecimal.valueOf(1000), true,
                new Bookingdates("2026-01-01", "2026-01-10"), "Breakfast"
        );
        bookingResponse = AuthManager.sendBookingRequest(bookingRequest);
    }

    @Then("бронирование успешно создано и получен его идентификатор")
    public void verifyBookingCreated() {
        bookingResponse.then().log().ifValidationFails().statusCode(200);
        bookingId = bookingResponse.body().path("bookingid");
    }

    @When("отправлен запрос на удаление созданного бронирования с использованием токена")
    public void deleteBookingWithToken() {
        deleteResponse = AuthManager.sendDeleteBookingRequest(bookingId, authToken);
    }

    @Then("бронирование успешно удалено из системы со статусом {int}")
    public void verifyBookingDeleted(int expectedStatus) {
        deleteResponse.then().log().ifValidationFails().statusCode(expectedStatus);
    }

    @When("отправлен запрос на авторизацию с логином {string} и паролем {string}")
    public void sendInvalidAuthRequest(String username, String password) {
        AuthRequest request = new AuthRequest(username, password);
        authResponse = AuthManager.sendAuthRequest(request);
    }

    @Then("сервис возвращает ошибку авторизации со статусом {int} и причиной {string}")
    public void verifyAuthBadCredentials(int expectedStatus, String expectedReason) {
        authResponse.then()
                .log().ifValidationFails()
                .statusCode(expectedStatus)
                .body("reason", is(expectedReason));
    }

    @When("отправлен запрос на авторизацию с некорректным телом {string} и типом контента {string}")
    public void sendInvalidBodyAuthRequest(String payload, String contentTypeStr) {
        authResponse = AuthManager.sendAuthRequestWithPayload(payload);
    }

    @Then("сервис возвращает ошибку валидации запроса со статусом {int}")
    public void verifyBadRequestStatus(int expectedStatus) {
        authResponse.then()
                .log().ifValidationFails()
                .statusCode(expectedStatus);
    }
}
