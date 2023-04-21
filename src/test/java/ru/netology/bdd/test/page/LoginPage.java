package ru.netology.bdd.test.page;

import ru.netology.bdd.test.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        $("[data-test-id=login] input").setValue(info.getLogin());
        $("[data-test-id=password] input").setValue(info.getPassword());
        $("[data-test-id=action-login]").click();
        return new VerificationPage();
    }

/*
    второй вариант страницы LoginPage:
    public class LoginPageV2 {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
     }

     третий вариант страницы LoginPage:
     public class LoginPageV3 {
    @FindBy(css = "[data-test-id=login] input")
    private SelenideElement loginField;
    @FindBy(css = "[data-test-id=password] input")
    private SelenideElement passwordField;
    @FindBy(css = "[data-test-id=action-login]")
    private SelenideElement loginButton;

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return page(VerificationPage.class);
   toDo: если VerificationPage была бы написана с помощью page factory, то LoginPageV3 вместо return page(VerificationPage.class); нужно было бы указать просто return;
    }
*/

}
