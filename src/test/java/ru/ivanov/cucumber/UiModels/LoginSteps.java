package ru.ivanov.cucumber.UiModels;


public class LoginSteps {
    private final LoginPage loginPage;

    public LoginSteps() {
        loginPage = new LoginPage();
    }

    public LoginSteps loginWithCredentials(String username, String password) {
        loginPage
                .setUsername(username)
                .setPassword(password)
                .clickLoginButton();
        return this;
    }

    public boolean isLogged(String loginText) {
        return loginPage.flashFieldHasText(loginText);
    }
}
