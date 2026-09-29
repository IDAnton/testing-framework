package ru.ivanov.cucumber;

import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import ru.ivanov.cucumber.UiModels.LoginSteps;

public class LoginStepDefinitions {
    private LoginSteps loginSteps;
    private String currentUsername;
    private String currentPassword;

    @Given("открыта страница авторизации")
    public void openAuthPage() {
        this.loginSteps = new LoginSteps();
    }

    @When("пользователь вводит имя {string} и пароль {string}")
    public void enterCredentials(String username, String password) {
        this.currentUsername = username;
        this.currentPassword = password;
    }

    @Then("система отображает ожидаемый текст сообщения {string}")
    public void verifyNotificationText(String expectedText) {
        boolean loginResult = loginSteps
                .loginWithCredentials(this.currentUsername, this.currentPassword)
                .isLogged(expectedText);

        Assertions.assertTrue(
                loginResult,
                String.format("Авторизация не вернула ожидаемый текст: '%s' для пользователя: '%s'",
                        expectedText, currentUsername)
        );
    }
}
