package ru.netology.bdd.test.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.bdd.test.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage { //личный кабинет
    //private SelenideElement heading = $("[data-test-id=dashboard]");
    // переменные класса приватные и, как правило, переменные страницы не показываем за класс (что в переменных объявлено используется только в методах этого класса) - принцип инкапсуляции
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = "р. ";
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private static final ElementsCollection cards = $$(".list__item div");

    // проверка видимости элемента (заголовка), когда создается объект страницы
    public DashboardPage() {
        heading.shouldBe(visible);
    }

    // Метод возвращает баланс карты, внутри коллекции ElementsCollection cards ищет конкретную строку, получает ее текст, сохраняет его в переменную text и передает его методу extractBalance и возвращает в переменную int числовое значение баланса карты:



    public int getCardBalance(DataHelper.CardInfo cardInfo) { // передаем в метод информацию о карте
        var text = cards.findBy(text(cardInfo.getCardNumber().substring(15))).getText(); //ищем по последним четырем знакам разряда номера карты
        return extractBalance(text);
    }

    /* метод ищет внутри коллекции ElementsCollection cards конкретную строку и нажимает на кнопку напротив карты:
    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo){
        cards.findBy(text(cardInfo.getCardNumber().substring(15))).$("button").click();
        return new TransferPage();
    }
    / метод ищет конкретную строку по индексу и нажимает на кнопку напротив карты (не лучший вариант поиска):
    public TransferPage selectCardToTransfer(int index){ нужно помнить, что коллекции в JAVA начинаются с 0.
        cards.get(index).$("button").click();
        return new TransferPage();
    }
    */

    //метод ищет конкретную строку по атрибуту "data-test-id" и значению тестовой метки хранящейся в объекте с информацией о карте cardInfo и нажимает на кнопку:
    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
        cards.findBy(attribute("data-test-id", cardInfo.getTestId())).$("button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}
// ToDo: Методы PageObject не нужно делать static (вызывается без создания экземпляра класса) приводит к потере контроля при синхронизации между созданием экземпляра класса и обращениям к методам (например к методу DashboardPage можно обращаться, только после того, как его создали и сохранили в переменную).