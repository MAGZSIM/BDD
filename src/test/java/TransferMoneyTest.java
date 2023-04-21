import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.bdd.test.data.DataHelper;
import ru.netology.bdd.test.page.DashboardPage;
import ru.netology.bdd.test.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.bdd.test.data.DataHelper.*;

public class TransferMoneyTest {
    LoginPage loginPage;
    DashboardPage dashboardPage; // объявляем dashboardPage как переменную экземпляра класса, чтобы работать с этой страницей в нескольких методах

    @BeforeEach
        // перед каждым тестом
    void setup() {
        loginPage = open("http://localhost:9999", LoginPage.class); // открытие страницы логина
        var authInfo = DataHelper.getAuthInfo(); // получение данных аутентификации
        var verificationPage = loginPage.validLogin(authInfo); // переход на страницу верификации
        var verificationCode = DataHelper.getVerificationCode(); // получение данных для верификации (код верификации)
        dashboardPage = verificationPage.validVerify(verificationCode); // Переход на страницу dashboardPage, т.к. dashboardPage объявлена как переменная экземпляра класса, var использовать не нужно, иначе она станет переменной метода, и дальше ее другие методы видеть не будут
    }


    @Test
    void shouldTransferFromFirstCardToSecondCard() {
        var firstCardInfo = getFirstCardInfo(); // получаем данные 1 карты
        var secondCardInfo = getSecondCardInfo(); // получаем данные 2 карты
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo); // получаем баланс 1 карты
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo); // получаем баланс 2 карты
        var amount = generateValidAmount(firstCardBalance); // в метод класса DataHelper передаем баланс карты списания и получаем случайную валидную сумму перевода внутри баланса карты
        var expectedBalanceFirstCard = firstCardBalance - amount; // ожидаемый баланс 1 карты
        var expectedBalanceSecondCard = secondCardBalance + amount; // ожидаемый баланс 2 карты
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo); // выбираем карту и попадаем на страницу перевода средств
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo); // со страницы перевода средств выполняем метод валидного перевода средств, кот. возвращает нам объект дашборда и сохраняем новую страницу в объявленную переменную dashboardPage и работаем с новым объектом
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo); // фактический баланс 1 карты
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo); // фактический баланс 2 карты
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard); // соответствие ожидаемого и фактического баланса 1 карты
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard); // соответствие ожидаемого и фактического баланса 2 карты
    }

    @Test
    void shouldGetErrorMessageIfAmountMoreBalance() {
        var firstCardInfo = getFirstCardInfo(); // получаем данные 1 карты
        var secondCardInfo = getSecondCardInfo(); // получаем данные 2 карты
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo); // получаем баланс 1 карты
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo); // получаем баланс 2 карты
        var amount = generateInvalidAmount(secondCardBalance); // в метод класса DataHelper передаем баланс карты списания и получаем случайную невалидную сумму перевода гарантированно превышающую баланса карты
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo); // выбираем карту и попадаем на страницу перевода средств
        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo); // со страницы перевода средств выполняем метод невалидного перевода средств, остаемся на странице перевода средств
        transferPage.findErrorMessage("Выполнена попытка перевода суммы, превышающей остаток на карте списания"); // в течение 15 секунд ищем сообщение об ошибке, в DashboardPage нет никакого сообщения по конкретной ошибке
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo); // фактический баланс 1 карты
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo); // фактический баланс 2 карты
        assertEquals(firstCardBalance, actualBalanceFirstCard); // соответствие начального и фактического баланса 1 карты
        assertEquals(secondCardBalance, actualBalanceSecondCard); // соответствие начального и фактического баланса 2 карты
    }
}
