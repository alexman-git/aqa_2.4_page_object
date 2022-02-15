package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class TransferNegativeTests {

    @BeforeEach
    public void setUp() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @ParameterizedTest
    @CsvSource(value = {"transferAmountFieldIsEmpty,''",
            "transferAmountIsZero,0",
            "transferAmountExceedsAvailableCardBalance,15000"})
    void shouldNotTransferFrom2ndTo1stThenShouldShowError(String testcase, String amount) {
        var dashboardPage = new DashboardPage();
        var transferPage = dashboardPage.firstCardTransfer();
        transferPage.invalidAmountTransfer(amount, DataHelper.getSecondCardInfo());
        DashboardPage.restoreBalance();
    }

    @ParameterizedTest
    @CsvSource(value = {"transferAmountFieldIsEmpty,''",
            "transferAmountIsZero,0",
            "transferAmountExceedsAvailableCardBalance,15000"})
    void shouldNotTransferFrom1stTo2ndThenShouldShowError(String testcase, String amount) {
        var dashboardPage = new DashboardPage();
        var transferPage = dashboardPage.secondCardTransfer();
        transferPage.invalidAmountTransfer(amount, DataHelper.getFirstCardInfo());
        DashboardPage.restoreBalance();
    }

    @Test
    void shouldNotTransferFrom2ndTo1stIfCardNumberFieldEmpty() {
        var dashboardPage = new DashboardPage();
        String amount = String.valueOf(5000);
        var transferPage = dashboardPage.firstCardTransfer();
        transferPage.emptyCardNumberTransfer(amount);
    }

    @Test
    void shouldNotTransferFrom1stTo2ndIfCardNumberFieldEmpty() {
        var dashboardPage = new DashboardPage();
        String amount = String.valueOf(5000);
        var transferPage = dashboardPage.secondCardTransfer();
        transferPage.emptyCardNumberTransfer(amount);
    }

    @Test
    void shouldNotTransferFrom2ndTo1stIfCardNumberIsTheSame() {
        var dashboardPage = new DashboardPage();
        String amount = String.valueOf(5000);
        var transferPage = dashboardPage.firstCardTransfer();
        transferPage.invalidCardNumberTransfer(amount, DataHelper.getFirstCardInfo());
    }

    @Test
    void shouldNotTransferFrom1stTo2ndIfCardNumberIsTheSame() {
        var dashboardPage = new DashboardPage();
        String amount = String.valueOf(5000);
        var transferPage = dashboardPage.secondCardTransfer();
        transferPage.invalidCardNumberTransfer(amount, DataHelper.getSecondCardInfo());
    }

    @Test
    void shouldNotTransferFrom2ndTo1stIfCardNumberIsInvalid() {
        var dashboardPage = new DashboardPage();
        String amount = String.valueOf(5000);
        var transferPage = dashboardPage.firstCardTransfer();
        transferPage.invalidCardNumberTransfer(amount, DataHelper.getInvalidCardInfo());
    }

    @Test
    void shouldNotTransferFrom1stTo2ndIfCardNumberIsInvalid() {
        var dashboardPage = new DashboardPage();
        String amount = String.valueOf(5000);
        var transferPage = dashboardPage.secondCardTransfer();
        transferPage.invalidCardNumberTransfer(amount, DataHelper.getInvalidCardInfo());
    }
}
