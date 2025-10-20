package com.example.lotterywebautotest.tests;

import com.example.lotterywebautotest.pages.HomePage;
import com.example.lotterywebautotest.pages.LoginPage;
import com.example.lotterywebautotest.pages.LotteryPage;
import com.example.lotterywebautotest.pages.RegisterPage;
import com.example.lotterywebautotest.utils.TestDataProvider;
import com.example.lotterywebautotest.utils.WebDriverUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 抽奖核心模块测试类
 */
public class LotteryCoreTest {

    private WebDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private LotteryPage lotteryPage;

    @BeforeMethod
    public void setUp() {
        driver = WebDriverUtils.getDriver();
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);
        lotteryPage = new LotteryPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * 测试抽奖功能正常流程
     */
    @Test(description = "验证抽奖功能正常流程")
    public void testLotteryDrawNormalFlow() throws InterruptedException {

        // 用户登录
        loginAsUser();

        // 点击活动按钮
        homePage.clickActivitiesLink();

        // 点击去抽奖
        WebElement goLottery = driver.findElement(By.xpath("//*[@id=\"activities-grid\"]/div[2]/div[3]/button"));
        goLottery.click();

        // 验证抽奖页面加载
        lotteryPage.verifyLotteryPageLoaded();

        // 验证抽奖网格展示
        lotteryPage.verifyLotteryGridDisplayed();


        // 验证活动信息展示
        lotteryPage.verifyActivityInfoDisplayed();

        // 验证奖品信息展示
        lotteryPage.verifyPrizesInfoDisplayed();


        // 验证抽奖按钮状态
        lotteryPage.verifyDrawButtonState(true);


        // 点击抽奖按钮
        lotteryPage.clickDrawButton();

        Thread.sleep(4000);
        // 验证抽奖结果展示
        lotteryPage.verifyDrawResultDisplayed();

//        // 切换到弹窗（Selenium 需先切换上下文才能操作弹窗）
//        Alert alert = driver.switchTo().alert();
//
//        // 处理弹窗：
//        // - 如果需要“确认禁用”，用 accept()；
//        // - 如果需要“取消禁用”，用 dismiss()。
//        alert.accept(); // 这里选择“确认禁用”，根据测试需求调整

        // 验证抽奖功能性能
        lotteryPage.verifyDrawPerformance();
    }

    private void loginAsUser() {
        // 访问首页
        driver.get("http://101.42.36.43:8888");

        // 验证首页加载
        homePage.verifyHomePageLoaded();

        // 点击登录按钮
        homePage.clickLoginButton();

        // 验证登录页面加载
        loginPage.verifyLoginPageLoaded();

        // 执行登录操作
        String[] userData = TestDataProvider.getTestUserData();
        String emailOrPhone = userData[2]; // 邮箱
        String password = userData[1]; // 密码
        String identity = "普通用户";

        loginPage.loginWithPassword(emailOrPhone, password, identity);

        // 验证登录成功
        loginPage.verifyLoginSuccess();
    }

    /**
     * 测试抽奖功能性能
     */
    @Test(description = "验证抽奖功能性能")
    public void testLotteryDrawPerformance() {
        // 用户登录
        loginAsUser();

        // 点击去抽奖
        homePage.clickActivitiesLink();

        WebElement goLottery = driver.findElement(By.xpath("//*[@id=\"activities-grid\"]/div[2]/div[3]/button"));
        goLottery.click();

        // 验证抽奖页面加载
        lotteryPage.verifyLotteryPageLoaded();

        // 验证抽奖功能性能
        lotteryPage.verifyDrawPerformance();
    }


    /**
     * 测试抽奖次数管理
     */
    @Test(description = "验证抽奖次数管理功能")
    public void testLotteryChancesManagement() throws InterruptedException {
        // 用户登录
        loginAsUser();

        // 点击去抽奖
        homePage.clickActivitiesLink();

        WebElement goLottery = driver.findElement(By.xpath("//*[@id=\"activities-grid\"]/div[1]/div[3]/button"));
        goLottery.click();

        // 验证抽奖页面加载
        lotteryPage.verifyLotteryPageLoaded();

        // 获取用户抽奖次数
        int initialChances = lotteryPage.getUserChances();
        System.out.println(initialChances);

        Thread.sleep(2000);
        // 点击抽奖按钮
        lotteryPage.clickDrawButton();

        Thread.sleep(3000);

        // 验证抽奖结果展示
        lotteryPage.verifyDrawResultDisplayed();

//        // 刷新页面获取最新抽奖次数
//        driver.navigate().refresh();

        lotteryPage.verifyLotteryPageLoaded();

        Thread.sleep(3000);

        // 验证抽奖次数是否减少
        int updatedChances = lotteryPage.getUserChances();
        System.out.println(updatedChances);
        assert updatedChances < initialChances : "抽奖次数应该减少";
    }
    /**
     * 测试抽奖网格展示
     */
    @Test(description = "验证抽奖网格展示功能")
    public void testLotteryGridDisplay() {
        // 用户登录
        loginAsUser();

        // 点击去抽奖
        homePage.clickActivitiesLink();

        WebElement goLottery = driver.findElement(By.xpath("//*[@id=\"activities-grid\"]/div[1]/div[3]/button"));
        goLottery.click();

        // 验证抽奖页面加载
        lotteryPage.verifyLotteryPageLoaded();

        // 验证抽奖网格展示
        lotteryPage.verifyLotteryGridDisplayed();

        // 验证网格项信息完整性
        lotteryPage.verifyGridItemInformationCompleteness();
    }

    /**
     * 测试抽奖页面信息展示
     */
    @Test(description = "验证抽奖页面信息展示功能")
    public void testLotteryPageInformationDisplay() {
        // 用户登录
        loginAsUser();

        // 点击去抽奖
        homePage.clickActivitiesLink();

        WebElement goLottery = driver.findElement(By.xpath("//*[@id=\"activities-grid\"]/div[1]/div[3]/button"));
        goLottery.click();

        // 验证抽奖页面加载
        lotteryPage.verifyLotteryPageLoaded();

        // 验证活动信息展示
        lotteryPage.verifyActivityInfoDisplayed();

        // 验证奖品信息展示
        lotteryPage.verifyPrizesInfoDisplayed();

        // 验证奖品信息完整性
        lotteryPage.verifyPrizeInformationCompleteness();
    }

    /**
     * 测试抽奖页面加载性能
     */
    @Test(description = "验证抽奖页面加载性能")
    public void testLotteryPageLoadPerformance() {
        // 用户登录
        loginAsUser();

        // 点击去抽奖
        homePage.clickActivitiesLink();



        // 测试抽奖页面加载性能
        long startTime = System.currentTimeMillis();

        WebElement goLottery = driver.findElement(By.xpath("//*[@id=\"activities-grid\"]/div[1]/div[3]/button"));
        goLottery.click();

        lotteryPage.verifyLotteryPageLoaded();
        long endTime = System.currentTimeMillis();

        long loadTime = endTime - startTime;
        assert loadTime < 8000 : "抽奖页面加载时间过长: " + loadTime + "ms";
    }

}

