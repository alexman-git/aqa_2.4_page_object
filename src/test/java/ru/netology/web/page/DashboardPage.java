package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id='dashboard']");

    private SelenideElement transferToFirstCard = $$("[data-test-id='action-deposit']").first();
    private SelenideElement transferToSecondCard = $$("[data-test-id='action-deposit']").last();

    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible).shouldHave(text("Личный кабинет"));
    }

    public int getCardBalance(int index) {
        var text = cards.get(index).text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransferPage firstCardTransfer() {
        transferToFirstCard.click();
        return new TransferPage();
    }

    public TransferPage secondCardTransfer() {
        transferToSecondCard.click();
        return new TransferPage();
    }

    public static void restoreBalance() {
        var dashboardPage = new DashboardPage();
        int currentBalanceFirstCard = dashboardPage.getCardBalance(0);
        int currentBalanceSecondCard = dashboardPage.getCardBalance(1);
        int restoreAmount;
        if (currentBalanceFirstCard > currentBalanceSecondCard) {
            restoreAmount = (currentBalanceFirstCard - currentBalanceSecondCard) / 2;
            var cardTransferPage = dashboardPage.secondCardTransfer();
            cardTransferPage.validTransfer(String.valueOf(restoreAmount), DataHelper.getFirstCardInfo());
        }
        if (currentBalanceSecondCard > currentBalanceFirstCard) {
            restoreAmount = (currentBalanceSecondCard - currentBalanceFirstCard) / 2;
            var cardTransferPage = dashboardPage.firstCardTransfer();
            cardTransferPage.validTransfer(String.valueOf(restoreAmount), DataHelper.getSecondCardInfo());
        }
    }
}
