package ru.netology.bdd.test.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.bdd.test.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement transferButton = $("[data-test-id = 'action-transfer']");
    private final SelenideElement amountInput = $("[data-test-id = 'amount'] input");
    private final SelenideElement fromInput = $("[data-test-id = 'from'] input");
    private final SelenideElement transferHead = $(byText("Пополнение карты"));
    private final SelenideElement errorMessage = $("[data-test-id = 'error-message']"); //нет такого селектора в DashboardPage


    public TransferPage() {
        transferHead.shouldBe(visible);
    }

    // метод для валидного перевода, ожидающий открытие новой страницы, после перевода средств:
    public DashboardPage makeValidTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) { //возвращает объект DashboardPage с помощью return
        makeTransfer(amountToTransfer, cardInfo);
        return new DashboardPage(); //создает новый экземпляр класса DashboardPage
    }

    // метод позволяющий осуществить перевод между картами:
    public void makeTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) { //void значит, что метод ничего не возвращает, просто выполни операции
        amountInput.setValue(amountToTransfer); // заполнили поле ввода суммы
        fromInput.setValue(cardInfo.getCardNumber()); // заполнили поле ввода карты списания
        transferButton.click(); // нажали на кнопку и остаемся на странице DashboardPage, это позволит нам поискать сообщения об ошибке
    }

    // метод позволяющий найти сообщение об ошибке:
    public void findErrorMessage(String expectedText) {
        errorMessage.shouldHave(exactText(expectedText), Duration.ofSeconds(15)).shouldBe(visible);
    }

}
