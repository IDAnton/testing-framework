package ru.ivanov.cucumber.UiModels;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.aeonbits.owner.ConfigFactory;
import ru.ivanov.cucumber.UrlConfigs.UrlConfigurator;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {
    private final static String baseUrl = ConfigFactory.create(UrlConfigurator.class).loadingUrl();
    private final SelenideElement usernameFiled = $("#username");
    private final SelenideElement passwordFiled = $("#password");
    private final SelenideElement flashField = $("#flash");
    private final SelenideElement loginButton = $("button[type='submit']");

    public LoginPage() {
        open(baseUrl);
    }

    public LoginPage setUsername(String username) {
        usernameFiled.setValue(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordFiled.setValue(password);
        return this;
    }

    public LoginPage clickLoginButton() {
        loginButton.click();
        return this;
    }

    boolean flashFieldHasText(String expectedError) {
        return flashField
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text(expectedError))
                .isDisplayed();
    }
}
