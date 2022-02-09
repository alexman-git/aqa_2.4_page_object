package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper.CardInfo;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement headerTransferPage = $(byText("Пополнение карты"));
    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromField = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    public final SelenideElement errorMessage = $("[data-test-id='error-notification'] .notification__content");

    public TransferPage() {
        headerTransferPage.shouldBe(visible);
    }

    public DashboardPage validTransfer(String amount, CardInfo cardNumber) {
        amountField.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        amountField.setValue(amount);
        fromField.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        fromField.setValue(cardNumber.getNumber());
        transferButton.click();
        return new DashboardPage();
    }

    public String invalidAmountTransfer(String amount, CardInfo cardNumber) {
        amountField.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        amountField.setValue(amount);
        fromField.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        fromField.setValue(cardNumber.getNumber());
        transferButton.click();
        return errorMessage.shouldBe(visible).text();
    }

    public String emptyCardNumberTransfer(String amount) {
        amountField.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        amountField.setValue(amount);
        fromField.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        transferButton.click();
        return errorMessage.shouldBe(visible).text();
    }

    public String invalidCardNumberTransfer(String amount, CardInfo cardNumber) {
        amountField.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        amountField.setValue(amount);
        fromField.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        fromField.setValue(cardNumber.getNumber());
        transferButton.click();
        return errorMessage.shouldBe(visible).text();
    }
}
