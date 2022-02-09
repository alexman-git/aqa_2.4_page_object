package ru.netology.web.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransferPositiveTests {

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
    @CsvSource(value = {"transferAmountLessThanStartBalance,5000",
            "transferAmountEqualsStartBalance,10000"})
    void shouldTransferFromSecondToFirstCard(String testcase, String amount) {
        var dashboardPage = new DashboardPage();
        var currentBalanceFirstCard = dashboardPage.getCardBalance(0);
        var currentBalanceSecondCard = dashboardPage.getCardBalance(1);
        var transferPage = dashboardPage.firstCardTransfer();
        transferPage.validTransfer(amount, DataHelper.getSecondCardInfo());
        assertEquals(currentBalanceFirstCard + Integer.parseInt(amount), dashboardPage.getCardBalance(0));
        assertEquals(currentBalanceSecondCard - Integer.parseInt(amount), dashboardPage.getCardBalance(1));
    }

    @ParameterizedTest
    @CsvSource(value = {"transferAmountLessThanStartBalance,5000",
            "transferAmountEqualsStartBalance,10000"})
    void shouldTransferFromFirstToSecondCard(String testcase, String amount) {
        var dashboardPage = new DashboardPage();
        var currentBalanceFirstCard = dashboardPage.getCardBalance(0);
        var currentBalanceSecondCard = dashboardPage.getCardBalance(1);
        var cardTransferPage = dashboardPage.secondCardTransfer();
        cardTransferPage.validTransfer(amount, DataHelper.getFirstCardInfo());
        assertEquals(currentBalanceFirstCard - Integer.parseInt(amount), dashboardPage.getCardBalance(0));
        assertEquals(currentBalanceSecondCard + Integer.parseInt(amount), dashboardPage.getCardBalance(1));
    }

    @ParameterizedTest
    @CsvSource(value = {"transferFractionPointZeroPlusTwoDigits,'0.45'",
            "transferFractionPointZeroPlusTwoDigits,'10.4'",
            "transferFractionByPointZeroPlusTwoDigits,'10.45'"})
    void shouldTransferFrom2ndTo1stCardAmountWithFraction(String testcase, String amount) {
        var dashboardPage = new DashboardPage();
        var currentBalanceFirstCard = dashboardPage.getCardBalance(0);
        var currentBalanceSecondCard = dashboardPage.getCardBalance(1);
        var cardTransferPage = dashboardPage.firstCardTransfer();
        cardTransferPage.validTransfer(amount, DataHelper.getSecondCardInfo());
        assertEquals(currentBalanceFirstCard + Float.parseFloat(amount), dashboardPage.getCardBalance(0));
        assertEquals(currentBalanceSecondCard - Float.parseFloat(amount), dashboardPage.getCardBalance(1));
    }

    @ParameterizedTest
    @CsvSource(value = {"transferFractionPointZeroPlusTwoDigits,'0.45'",
            "transferFractionPointZeroPlusTwoDigits,'10.4'",
            "transferFractionByPointZeroPlusTwoDigits,'10.45'"})
    void shouldTransferFrom1stTo2ndCardAmountWithFraction(String testcase, String amount) {
        var dashboardPage = new DashboardPage();
        var currentBalanceFirstCard = dashboardPage.getCardBalance(0);
        var currentBalanceSecondCard = dashboardPage.getCardBalance(1);
        var cardTransferPage = dashboardPage.secondCardTransfer();
        cardTransferPage.validTransfer(amount, DataHelper.getFirstCardInfo());
        assertEquals(currentBalanceFirstCard - Float.parseFloat(amount), dashboardPage.getCardBalance(0));
        assertEquals(currentBalanceSecondCard + Float.parseFloat(amount), dashboardPage.getCardBalance(1));
    }
}
