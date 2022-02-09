package ru.netology.web.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import java.time.Duration;
import java.util.function.BooleanSupplier;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransferNegativeTests {

    @BeforeEach
    public void setUp() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @AfterEach
    public void restoreCards() {
        DashboardPage.restoreBalance();
    }

    @ParameterizedTest
    @CsvSource(value = {"transferAmountFieldIsEmpty,''",
            "transferAmountIsZero,0",
            "transferAmountExceedsAvailableCardBalance,15000"})
    void shouldNotTransferFrom2ndTo1stThenShouldShowError(String testcase, String amount) {
        var dashboardPage = new DashboardPage();
        var transferPage = dashboardPage.firstCardTransfer();
        transferPage.invalidAmountTransfer(amount, DataHelper.getSecondCardInfo());
        assertTrue((BooleanSupplier) transferPage.errorMessage.shouldBe(visible).shouldHave(text("Ошибка! Произошла ошибка"), Duration.ofSeconds(15)));
    }

    @ParameterizedTest
    @CsvSource(value = {"transferAmountFieldIsEmpty,''",
            "transferAmountIsZero,0",
            "transferAmountExceedsAvailableCardBalance,15000"})
    void shouldNotTransferFrom1stTo2ndThenShouldShowError(String testcase, String amount) {
        var dashboardPage = new DashboardPage();
        var transferPage = dashboardPage.secondCardTransfer();
        transferPage.invalidAmountTransfer(amount, DataHelper.getFirstCardInfo());
        assertTrue((BooleanSupplier) transferPage.errorMessage.shouldBe(visible).shouldHave(text("Ошибка! Произошла ошибка"), Duration.ofSeconds(15)));
    }

    @Test
    void shouldNotTransferFrom2ndTo1stIfCardNumberFieldEmpty() {
        var dashboardPage = new DashboardPage();
        String amount = String.valueOf(5000);
        var transferPage = dashboardPage.firstCardTransfer();
        transferPage.emptyCardNumberTransfer(amount);
        assertTrue((BooleanSupplier) transferPage.errorMessage.shouldBe(visible).shouldHave(text("Ошибка! Произошла ошибка"), Duration.ofSeconds(15)));
    }

    @Test
    void shouldNotTransferFrom1stTo2ndIfCardNumberFieldEmpty() {
        var dashboardPage = new DashboardPage();
        String amount = String.valueOf(5000);
        var transferPage = dashboardPage.secondCardTransfer();
        transferPage.emptyCardNumberTransfer(amount);
        assertTrue((BooleanSupplier) transferPage.errorMessage.shouldBe(visible).shouldHave(text("Ошибка! Произошла ошибка"), Duration.ofSeconds(15)));
    }

    @Test
    void shouldNotTransferFrom2ndTo1stIfCardNumberIsTheSame() {
        var dashboardPage = new DashboardPage();
        String amount = String.valueOf(5000);
        var transferPage = dashboardPage.firstCardTransfer();
        transferPage.invalidCardNumberTransfer(amount, DataHelper.getFirstCardInfo());
        assertTrue((BooleanSupplier) transferPage.errorMessage.shouldBe(visible).shouldHave(text("Ошибка! Произошла ошибка"), Duration.ofSeconds(15)));
    }

    @Test
    void shouldNotTransferFrom1stTo2ndIfCardNumberIsTheSame() {
        var dashboardPage = new DashboardPage();
        String amount = String.valueOf(5000);
        var transferPage = dashboardPage.secondCardTransfer();
        transferPage.invalidCardNumberTransfer(amount, DataHelper.getSecondCardInfo());
        assertTrue((BooleanSupplier) transferPage.errorMessage.shouldBe(visible).shouldHave(text("Ошибка! Произошла ошибка"), Duration.ofSeconds(15)));
    }

    @Test
    void shouldNotTransferFrom2ndTo1stIfCardNumberIsInvalid() {
        var dashboardPage = new DashboardPage();
        String amount = String.valueOf(5000);
        var transferPage = dashboardPage.firstCardTransfer();
        transferPage.invalidCardNumberTransfer(amount, DataHelper.getInvalidCardInfo());
        assertTrue((BooleanSupplier) transferPage.errorMessage.shouldBe(visible).shouldHave(text("Ошибка! Произошла ошибка"), Duration.ofSeconds(15)));
    }

    @Test
    void shouldNotTransferFrom1stTo2ndIfCardNumberIsInvalid() {
        var dashboardPage = new DashboardPage();
        String amount = String.valueOf(5000);
        var transferPage = dashboardPage.secondCardTransfer();
        transferPage.invalidCardNumberTransfer(amount, DataHelper.getInvalidCardInfo());
        assertTrue((BooleanSupplier) transferPage.errorMessage.shouldBe(visible).shouldHave(text("Ошибка! Произошла ошибка"), Duration.ofSeconds(15)));
    }
}
